(ns interfaces.rest.delete-user-http-adapter-integrated-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [interfaces.rest.delete-user-http-adapter :refer [create-delete-user-http-adapter MISSING_ID_ERROR_MESSAGE]]
   [infrastructure.repositories.in-memory-user-repository :refer [create-in-memory-repository]]
   [clojure.string :refer [includes?]]
   [ring.mock.request :as mock]))

(defn- send-delete-request [id]
  (let [repo (create-in-memory-repository (atom {"123" {:id "123", :email "user@example.com", :name "Existing User"}}))
        adapter (create-delete-user-http-adapter repo)
        request (mock/request :delete (str "/users/psql?id=" id))]
    (adapter (assoc request :query-params {"id" id}))))

(deftest test-delete-user-handler
  (testing "Successfully deleting an existing user"
    (let [response (send-delete-request "123")]
      (is (= 200 (:status response)))
      (is (= "{\"message\":\"User deleted successfully\"}" (:body response))))

  (testing "Attempt to delete a non-existing user"
    (let [response (send-delete-request "999")]
      (is (= 422 (:status response)))
      (is (includes? (:body response) "User not found."))))

    (testing "Invalid ID provided"
      (let [response (send-delete-request 1234567890)]
        (is (= 400 (:status response)))
        (is (includes? (:body response) MISSING_ID_ERROR_MESSAGE))))))