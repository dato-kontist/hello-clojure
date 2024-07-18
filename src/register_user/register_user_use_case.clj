(ns register-user.register-user-use-case)

(def USER_ALREADY_REGISTERED_ERROR
  "User already registered. ")

(defn- check-user-registered
  [user-repo email]
  (if (.find-by-email user-repo email)
    (throw (ex-info (str USER_ALREADY_REGISTERED_ERROR) {:email email}))
    nil))

(defn- create-user
  [user-repo user]
  (.create user-repo user)
  user)

(defn register-user
  [user-repo user]
  (check-user-registered user-repo (:email user))
  (let [createdUser (create-user user-repo user)]
    (-> createdUser)))
