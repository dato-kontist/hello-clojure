(ns infrastructure.repositories.psql-user-repository-test
  (:require
   [clojure.test :refer [deftest is]]
   [infrastructure.repositories.psql-user-repository :refer [create-in-psql-user-repository]]
   [toucan.db :as db]
   [infrastructure.db :as models]))

;; Mock implementations of database functions
(defn mock-select-one [model key value]
  ;; Simulate the behavior expected in tests
  (when (= model models/User)
    (if (= key :email)
      (if (= value "test@example.com")
        {:id 1 :email "test@example.com" :name "Test User"}
        nil)
      nil)))

(defn mock-insert! [model data]
  ;; Simulate the behavior expected in tests
  (when (= model models/User)
    (assoc data :id 1)))

(defn mock-select-one-by-id [model _ id]
  ;; Simulate finding user by ID
  (when (= model models/User)
    (if (= id 1)
      {:id 1 :email "test@example.com" :name "Test User"}
      nil)))

(defn mock-delete [model conditions]
  ;; Simulate deleting a user
  (when (= model models/User)
    (if (= (:id conditions) 1)
      1  ; Simulate that one record was deleted
      0)))  ; No records deleted

(deftest test-find-by-email
  (with-redefs [db/select-one mock-select-one]
    (let [repo (create-in-psql-user-repository nil)
          user (.find-by-email repo "test@example.com")] ; Use repo's method
      (is (= user {:id 1 :email "test@example.com" :name "Test User"})))))

(deftest test-create
  (with-redefs [db/insert! mock-insert!]
    (let [repo (create-in-psql-user-repository nil)
          new-user {:email "test@example.com" :name "Test User"}
          created-user (.create repo new-user)] ; Use repo's method
      (is (= created-user {:email "test@example.com" :name "Test User" :id 1})))))

(deftest test-find-by-id
  (with-redefs [db/select-one mock-select-one-by-id]
    (let [repo (create-in-psql-user-repository nil)
          user (.find-by-id repo 1)] ; Use repo's method to find by ID
      (is (= user {:id 1 :email "test@example.com" :name "Test User"})))))

(deftest test-delete
  (with-redefs [db/delete! mock-delete]
    (let [repo (create-in-psql-user-repository nil)
          delete-count (.delete repo 1)] ; Corrected to pass just the ID, not a map
      (is (= delete-count 1))))) ; Check if one record was reported as deleted