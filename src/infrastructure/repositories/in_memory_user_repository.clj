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
  (find-by-id [_ id]
    (let [user (get @db id)]
      (if user
        user
        nil)))
  (create [_ user]
    (swap! db assoc (:email user) user)
    (swap! db assoc (:id user) user)
    user)
  (delete [_ id]
    (let [user (get @db id)]
      (swap! db dissoc db (:email user))
      (swap! db dissoc db id)
      id)))

(defn create-in-memory-repository [db]
  (->InMemoryUserRepository db))