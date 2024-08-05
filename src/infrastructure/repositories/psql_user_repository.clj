(ns infrastructure.repositories.psql-user-repository
  (:require
   [infrastructure.db :as models]
   [toucan.db :as db]
   [domain.ports :refer [UserRepositoryPort]]))

(defrecord PSqlUserRepository [database]
  UserRepositoryPort
  (find-by-email [_ email]
    (db/select-one models/User :email email))
  (create [_ user]
    (db/insert! models/User user)))

(defn create-in-psql-user-repository [db]
  (->PSqlUserRepository db))