(ns application.commands.delete-user-use-case)

(def USER_NOT_FOUND_ERROR
  "User not found. ")

(defn- check-user-exists
  [user-repo id]
  (if-not (.find-by-id user-repo id)
    (throw (ex-info (str USER_NOT_FOUND_ERROR) {:id id}))
    true))

(defn- delete-user
  [user-repo id]
  (.delete user-repo id)
  {:status "success" :message (str "User with ID " id " has been deleted.")})

(defn delete-user-by-id
  [user-repo id]
  (check-user-exists user-repo id)
  (delete-user user-repo id))