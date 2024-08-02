(ns application.commands.register-user-use-case-test
  (:require [clojure.test :refer [deftest testing is]]
            [domain.ports :refer [UserRepositoryPort]]
            [application.commands.register-user-use-case :refer [register-user]]))

(deftest register-user-tests
  (testing "User already registered"
    (let [mock-repo (reify UserRepositoryPort
                      (find-by-email [_ _] true)
                      (create [_ _] nil))]
      (try
        (register-user mock-repo {:email "test@example.com"})
        (is false "Expected exception not thrown")
        (catch clojure.lang.ExceptionInfo e
          (is (= "User already registered. " (ex-message e)))
          (is (= {:email "test@example.com"} (ex-data e)))))))

  (testing "User registration successful"
    (let [mock-repo (reify UserRepositoryPort
                      (find-by-email [_ _] false)
                      (create [_ user] user))]
      (is (= {:email "newuser@example.com"}
             (register-user mock-repo {:email "newuser@example.com"}))))))