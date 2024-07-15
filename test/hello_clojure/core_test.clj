(ns hello-clojure.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [hello-clojure.core :refer [-main]]))

(deftest test-main
  (testing "No arguments"
    (is (= (with-out-str (-main)) "Hello, World!\n")))

  (testing "With a name argument"
    (is (= (with-out-str (-main "Alice")) "Hello, Alice!\n"))))