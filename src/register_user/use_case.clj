(ns register-user.use-case)

(def in-memory-user-db (atom {}))

(defn- check-user-registered
  "Checks if the user is already registered in the user-db."
  [email]
  (if (contains? @in-memory-user-db email)
    (throw (ex-info "User already registered" {:status 422}))
    nil))

(defn- createUser "Adds the user to the in-memory-user-db atom." [user]
  (swap! in-memory-user-db assoc (:email user) user)
  user)

(defn register-user
  "Registers a user and returns the response."
  [user]
  (check-user-registered (:email user))
  (let [createdUser (createUser user)]
    (-> createdUser)))