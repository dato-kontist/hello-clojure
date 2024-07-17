(ns register-user.use-case)

(defn- check-user-registered
  "Checks if the user is already registered in the user-db."
  [user-repo email]
  (if (.find-by-email user-repo email)
    (throw (ex-info "User already registered" {:status 422}))
    nil))

(defn- createUser
  "Adds the user to the user-repo."
  [user-repo user]
  (.create user-repo user)
  user)

(defn register-user
  "Registers a user and returns the response."
  [user-repo user]
  (check-user-registered user-repo (:email user))
  (let [createdUser (createUser user-repo user)]
    (-> createdUser)))
