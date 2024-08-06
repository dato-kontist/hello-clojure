(ns infrastructure.repositories.in-memory-user-repository-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [infrastructure.repositories.in-memory-user-repository :refer [create-in-memory-repository]]))

(deftest test-in-memory-user-repository
  (let [db (atom {})
        repo (create-in-memory-repository db)
        user {:id "1234567890" :email "test@example.com" :name "Test User"}]

    (testing "create user"
      (is (= user (.create repo user)))
      (is (= user (get @db "test@example.com"))))

    (testing "find user by email"
      (is (= user (.find-by-email repo "test@example.com")))
      (is (nil? (.find-by-email repo "nonexistent@example.com"))))

    (testing "find user by id"
      (is (= user (.find-by-id repo "1234567890")))
      (is (nil? (.find-by-id repo "0987654321"))))

    (testing "delete user by id"
      (is (= "1234567890" (.delete repo "1234567890")))
      (is (nil? (.find-by-id repo "1234567890"))))))
