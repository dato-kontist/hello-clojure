(ns register-user.adapter.psql-user-repository-test
  (:require
   [clojure.test :refer [deftest is]]
   [register-user.adapter.psql-user-repository :refer [create-in-psql-user-repository]]
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

