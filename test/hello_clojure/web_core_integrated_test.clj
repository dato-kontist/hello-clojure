(ns hello-clojure.web-core-integrated-test
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.util.response :refer [response]]
            [clojure.test :refer [deftest is testing run-tests]]
            [ring.mock.request :as mock]))

(defn handler [_]
  (response "Hello, World!"))

(defn -main []
  (run-jetty handler {:port 3000}))

;; Integrated test
(deftest test-handler
  (testing "GET request to handler"
    (let [request (mock/request :get "/")
          response (handler request)]
      (is (= 200 (:status response)))
      (is (= "Hello, World!" (:body response))))))

;; Run tests
(run-tests)