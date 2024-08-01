(ns domain.user)

(defrecord User [id email password])

(defn create-user [id email password]
  (User. id email password))
