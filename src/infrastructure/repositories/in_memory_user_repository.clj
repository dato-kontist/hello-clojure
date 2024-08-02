(ns infrastructure.repositories.in-memory-user-repository
  (:require
   [domain.ports :refer [UserRepositoryPort]]))

(defrecord InMemoryUserRepository [db]
  UserRepositoryPort
  (find-by-email [_ email]
    (let [user (get @db email)]
      (if user
        user
        nil)))
  (create [_ user]
    (swap! db assoc (:email user) user)
    user))

(defn create-in-memory-repository [db]
  (->InMemoryUserRepository db))