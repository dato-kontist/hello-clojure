(ns application.queries.find-user-by-email-use-case-test
  (:require [clojure.test :refer [deftest testing is]]
            [domain.ports :refer [UserRepositoryPort]]
            [application.queries.find-user-by-email-use-case :refer [find-user-by-email]]))

(defn mock-user-repo [expected-email]
  (reify UserRepositoryPort
    (find-by-email [_ e]
      (if (= e expected-email)
        {:id "123", :name "Test User", :email e}
        nil))))

(deftest find-user-by-email-test
  (testing "Find user by existing email"
    (let [email "test@example.com"
          user-repo (mock-user-repo email)
          result (find-user-by-email user-repo email)]
      (is (= {:id "123", :name "Test User", :email email} result))))

  (testing "Handle user not found scenario"
    (let [email "test@example.com"
          wrong-email "notfound@example.com"
          user-repo (mock-user-repo email)]
      (is (thrown-with-msg? Exception #"User not found. "
                            (find-user-by-email user-repo wrong-email))))))
