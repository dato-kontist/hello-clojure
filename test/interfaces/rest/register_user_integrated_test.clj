(ns interfaces.rest.register-user-integrated-test
  (:require [cheshire.core :as json]
            [clojure.test :refer [deftest is testing]]
            [clojure.string :refer [includes?]]
            [interfaces.rest.register-user-http-adapter :refer [create-register-user-http-adapter, INVALID_DATA_ERROR_MESSAGE, MISSING_EMAIL_ERROR_MESSAGE, MISSING_ID_ERROR_MESSAGE, MISSING_NAME_ERROR_MESSAGE]]
            [infrastructure.repositories.in-memory-user-repository :refer [create-in-memory-repository]]
            [application.commands.register-user-use-case :refer [USER_ALREADY_REGISTERED_ERROR]]
            [ring.mock.request :as mock]))

(def ^:private VALID_USER
  {:id "123" :email "test@example.com" :name "Test User"})

(defn- send-request [request]
  ((create-register-user-http-adapter (create-in-memory-repository (atom {}))) request))

(defn- create-request [user]
  (-> (mock/request :post "/")
      (mock/content-type "application/json")
      (mock/body (json/generate-string user))))

(deftest test-register-user-handler
  (testing "Valid user registration"
    (let [request (create-request VALID_USER)
          response (send-request request)]
      (is (= 200 (:status response)))
      (is (= VALID_USER (json/parse-string (:body response) true)))))

  (testing "Missing id"
    (let [request (create-request {:email "test@example.com" :name "Test User"})
          response (send-request request)]
      (is (= 400 (:status response)))
      (is (includes? (:body response) INVALID_DATA_ERROR_MESSAGE))
      (is (includes? (:body response) MISSING_ID_ERROR_MESSAGE))
      (is not (includes? (:body response) MISSING_EMAIL_ERROR_MESSAGE))
      (is not (includes? (:body response) MISSING_NAME_ERROR_MESSAGE)))))

(testing "Missing email"
  (let [request (create-request {:id "123" :name "Test User"})
        response (send-request request)]
    (is (= 400 (:status response)))
    (is (includes? (:body response) INVALID_DATA_ERROR_MESSAGE))
    (is (includes? (:body response) MISSING_EMAIL_ERROR_MESSAGE))
    (is not (includes? (:body response) MISSING_ID_ERROR_MESSAGE))
    (is not (includes? (:body response) MISSING_NAME_ERROR_MESSAGE))))

(testing "Missing name"
  (let [request (create-request {:id "123" :email "test@example.com"})
        response (send-request request)]
    (is (= 400 (:status response)))
    (is (includes? (:body response) INVALID_DATA_ERROR_MESSAGE))
    (is (includes? (:body response) MISSING_NAME_ERROR_MESSAGE))
    (is not (includes? (:body response) MISSING_ID_ERROR_MESSAGE))
    (is not (includes? (:body response) MISSING_EMAIL_ERROR_MESSAGE))))

(testing "User already registered"
  (let [adapter (create-register-user-http-adapter (create-in-memory-repository (atom {})))
        validRequest (create-request VALID_USER)
        validResponse (adapter validRequest)]
    (is (= 200 (:status validResponse)))
    (is (= VALID_USER (json/parse-string (:body validResponse) true)))
    (let [duplicatedUserRequest (create-request VALID_USER)
          unprocessableContentResponse (adapter duplicatedUserRequest)]
      (is (= 422 (:status unprocessableContentResponse)))
      (is (includes? (:body unprocessableContentResponse) USER_ALREADY_REGISTERED_ERROR)))))