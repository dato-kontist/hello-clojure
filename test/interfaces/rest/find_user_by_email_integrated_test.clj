(ns interfaces.rest.find-user-by-email-integrated-test
  (:require [cheshire.core :as json]
            [clojure.test :refer [deftest is testing]]
            [clojure.string :as string]
            [interfaces.rest.find-user-by-email-http-adapter :refer [create-find-user-by-email-http-adapter]]
            [infrastructure.repositories.in-memory-user-repository :refer [create-in-memory-repository]]
            [ring.mock.request :as mock]))

(def ^:private VALID_USER
  {:id "123", :email "test@example.com", :name "Test User"})

(defn- send-request [email]
  (let [user-repo (create-in-memory-repository (atom {(:email VALID_USER) VALID_USER}))
        adapter (create-find-user-by-email-http-adapter user-repo)
        request (-> (mock/request :get (str "/users/in-memory?email=" email))
                    (mock/content-type "application/json"))]
    (adapter (assoc request :query-params {"email" email}))))

(deftest test-find-user-by-email-handler
  (testing "Find existing user by email"
    (let [response (send-request "test@example.com")]
      (is (= 200 (:status response)))
      (is (= VALID_USER (json/parse-string (:body response) true))))

    (testing "User not found by email"
      (let [response (send-request "notfound@example.com")]
        (is (= 404 (:status response)))
        (is (string/includes? (:body response) "User not found"))))

    (testing "Invalid email format (bad request)"
      (let [response (send-request 1234567890)]
        (is (= 400 (:status response)))
        (is (string/includes? (:body response) "'email' must be a string"))))))