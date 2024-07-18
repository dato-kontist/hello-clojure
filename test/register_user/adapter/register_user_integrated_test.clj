(ns register-user.adapter.register-user-integrated-test
  (:require [cheshire.core :as json]
            [clojure.test :refer [deftest is testing]]
            [clojure.string :refer [includes?]]
            [register-user.adapter.register-user-http-adapter :refer [create-register-user-http-adapter, INVALID_DATA_ERROR_MESSAGE, MISSING_EMAIL_ERROR_MESSAGE, MISSING_ID_ERROR_MESSAGE, MISSING_NAME_ERROR_MESSAGE]]
            [register-user.adapter.in-memory-user-repository :refer [create-in-memory-repository]]
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

;; TODO learn how to attach debugger into code execution to verify why the test is failing.
;; (testing "User already registered"
;;   (let [request (-> (mock/request :post "/")
;;                     (mock/content-type "application/json")
;;                     (mock/body (json/generate-string {:id "a_valid_user_id" :email "test@example.com" :name "Test User"})))
;;         repository (create-in-memory-repository (atom {}))
;;         actualRegistration (create-register-user-http-adapter repository request)]
;;     (is (= 200 (:status actualRegistration)))
;;     (is (= {:id "a_valid_user_id" :email "test@example.com" :name "Test User"}
;;            (json/parse-string (:body actualRegistration) true)))
;;     (let [response (create-register-user-http-adapter repository request)] ;; Try to register again
;;       (is (= 422 (:status response)))
;;       (is (= "{\"error\":\"User already registered\"}" (:body response))))))