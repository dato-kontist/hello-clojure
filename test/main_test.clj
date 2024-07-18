(ns main-test
  (:require [clojure.test :refer [deftest is use-fixtures testing]]
            [clojure.data.json :as json]
            [clj-http.client :as client]
            [main :refer [-main]]))

(defonce server (atom nil))

(defn start-test-server []
  (reset! server (-main :join? false))
  (Thread/sleep 1000))

(defn stop-test-server []
  (when @server
    (.stop @server)
    (reset! server nil)
    (Thread/sleep 1000)))

(use-fixtures :once
  (fn [tests]
    (start-test-server)
    (tests)
    (stop-test-server)))

(deftest test-main
  (testing "Server responds to /register-user"
    (let [response (client/post "http://localhost:3000/register-user"
                                {:body (json/write-str {:id "123" :email "test@example.com" :name "Test User"})
                                 :headers {"Content-Type" "application/json"}})]
      (is (= 200 (:status response))))))