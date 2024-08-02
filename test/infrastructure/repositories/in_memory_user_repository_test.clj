(ns infrastructure.repositories.in-memory-user-repository-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [infrastructure.repositories.in-memory-user-repository :refer [create-in-memory-repository]]))

(deftest test-in-memory-user-repository
  (let [db (atom {})
        repo (create-in-memory-repository db)
        user {:email "test@example.com" :name "Test User"}]

    (testing "create user"
      (is (= user (.create repo user)))
      (is (= user (get @db "test@example.com"))))

    (testing "find user by email"
      (is (= user (.find-by-email repo "test@example.com")))
      (is (nil? (.find-by-email repo "nonexistent@example.com"))))))
