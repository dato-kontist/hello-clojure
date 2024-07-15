(ns hello-clojure.web-core-test
  (:require [clojure.test :refer [deftest is testing]]
            [ring.mock.request :as mock]
            [hello-clojure.web-core :refer [handler]]))

(deftest test-handler
  (testing "GET request to /"
    (let [response (handler (mock/request :get "/"))]
      (is (= 200 (:status response)))
      (is (= "Hello, World!" (:body response))))))