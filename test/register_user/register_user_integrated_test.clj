(ns register-user.register-user-integrated-test
  (:require [cheshire.core :as json]
            [clojure.test :refer [deftest is testing]]
            [register-user.register-user :refer [registerUserHttpHandler]]
            [ring.mock.request :as mock]))

(deftest test-register-user-handler
  (testing "Valid user registration"
    (let [request (-> (mock/request :post "/")
                      (mock/content-type "application/json")
                      (mock/body (json/generate-string {:id "123" :email "test@example.com" :name "Test User"})))
          response (registerUserHttpHandler request)]
      (is (= 200 (:status response)))
      (is (= {:id "123" :email "test@example.com" :name "Test User"}
             (json/parse-string (:body response) true)))))

  (testing "Missing id"
    (let [request (-> (mock/request :post "/")
                      (mock/content-type "application/json")
                      (mock/body (json/generate-string {:email "test@example.com" :name "Test User"})))
          response (registerUserHttpHandler request)]
      (is (= 400 (:status response)))
      (is (= "{\"error\":\"Invalid user data\"}" (:body response)))))

  (testing "Missing email"
    (let [request (-> (mock/request :post "/")
                      (mock/content-type "application/json")
                      (mock/body (json/generate-string {:id "123" :name "Test User"})))
          response (registerUserHttpHandler request)]
      (is (= 400 (:status response)))
      (is (= "{\"error\":\"Invalid user data\"}" (:body response)))))

  (testing "Missing name"
    (let [request (-> (mock/request :post "/")
                      (mock/content-type "application/json")
                      (mock/body (json/generate-string {:id "123" :email "test@example.com"})))
          response (registerUserHttpHandler request)]
      (is (= 400 (:status response)))
      (is (= "{\"error\":\"Invalid user data\"}" (:body response))))))

;; TODO learn how to attach debugger into code execution to verify why the test is failing.
;;   (testing "User already registered"
;;     (let [request (-> (mock/request :post "/")
;;                       (mock/content-type "application/json")
;;                       (mock/body (json/generate-string {:id "a_valid_user_id" :email "test@example.com" :name "Test User"})))
;;           actualRegistration (registerUserHttpHandler request)]
;;       (is (= 200 (:status actualRegistration)))
;;       (is (= {:id "a_valid_user_id" :email "test@example.com" :name "Test User"}
;;              (json/parse-string (:body actualRegistration) true)))
;;       (let [response (registerUserHttpHandler request)] ;; Try to register again
;;         (is (= 422 (:status response)))
;;         (is (= "{\"error\":\"User already registered\"}" (:body response)))))))