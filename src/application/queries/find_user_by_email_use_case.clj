(ns application.queries.find-user-by-email-use-case)

(def USER_NOT_FOUND
  "User not found. ")

(defn find-user-by-email
  [user-repo email]
  (let [user (.find-by-email user-repo email)]
    (if user
      user
      (throw (ex-info (str USER_NOT_FOUND) {:email email})))))