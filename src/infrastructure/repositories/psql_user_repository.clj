(ns infrastructure.repositories.psql-user-repository
  (:require
   [infrastructure.db :as models]
   [toucan.db :as db]
   [domain.ports :refer [UserRepositoryPort]]))

(defrecord PSqlUserRepository [database]
  UserRepositoryPort
  (find-by-email [_ email]
    (db/select-one models/User :email email))
  (find-by-id [_ id]
    (db/select-one models/User :id id))
  (create [_ user]
    (db/insert! models/User user))
  (delete [_ id]
    (db/delete! models/User {:id id})))

(defn create-in-psql-user-repository [db]
  (->PSqlUserRepository db))