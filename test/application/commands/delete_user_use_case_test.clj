(ns application.commands.delete-user-use-case-test
  (:require [clojure.test :refer [deftest is testing]]
            [application.commands.delete-user-use-case :refer [delete-user-by-id]]
            [infrastructure.repositories.in-memory-user-repository :refer [create-in-memory-repository]]))

(defn- setup-test-db []
  (atom {1 {:id 1 :email "user1@example.com" :name "User One"}
         2 {:id 2 :email "user2@example.com" :name "User Two"}}))

(deftest test-delete-user-by-id
  (testing "Deleting an existing user"
    (let [db (setup-test-db)
          repo (create-in-memory-repository db)]
      (.create repo {:id 3 :email "user3@example.com" :name "User Three"})
      (is (= {:status "success" :message "User with ID 3 has been deleted."}
             (delete-user-by-id repo 3)))
      (is (nil? (.find-by-id repo 3)) "User should no longer exist in repository after deletion")))

  (testing "Attempting to delete a non-existing user"
    (let [db (setup-test-db)
          repo (create-in-memory-repository db)]
      (is (thrown? Exception
                   (delete-user-by-id repo 999))
          "Expected exception for non-existing user ID"))))