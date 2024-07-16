;; ns declares a new namespace to be used/required in other files.
(ns register-user.register-user
  ;; require jetty handler to start the web servlet.
  (:require ;; require ring response handler to create web-server responses.
            [ring.util.response :refer [response]]
            ;; json parsing library.
            [cheshire.core :as json]))

(defn- parse-user
  "Parses the user data from the request body."
  [body]
  ;; parse body into user variable
  (let [user (json/parse-string body true)

        ;; check if user has id, email, and name
        ;; if not, add error to errors vector
        ;; vector data structure is indexed, sequential, immutable and persistent. 
        errors (cond-> []
                 ;; conj adds items to the end of the vector.
                 (not (string? (:id user))) (conj "id must be a string")
                 (not (string? (:email user))) (conj "email must be a string")
                 (not (string? (:name user))) (conj "name must be a string"))]
    (if (empty? errors)
      user
      ;; throw an exception if errors are not empty
      ;; the order of the errors is to be preserved. If id is missing it will be always the first in the list.
      (throw (ex-info "Invalid user data" {:status 400 :errors errors})))))

;; defines a map to store users in memory.
(def in-memory-user-db (atom {}))

;; defn- defines a private method.
(defn- check-user-registered
  "Checks if the user is already registered in the user-db."
  [email]
  ;; when email is already in the map, throw an exception.
  ;; accessed in constant time since list is indexed by email.
  (if (contains? @in-memory-user-db email)
    ;; ex-info creates an exception with a message and data.
    (throw (ex-info "User already registered" {:status 422}))
    nil))

(defn- createUser "Adds the user to the in-memory-user-db atom." [user]
  ;; swap! updates value on the object
  ;; assoc associates key with valule in the object
  ;; in-memory-user-db is the object, (:email user) is the key and user is the value.
  (swap! in-memory-user-db assoc (:email user) user)
  user)

(defn registerUserHttpHandler
  "Handles the HTTP request to register a user."
  [request]
  (try
    ;; slurp reads the body of the request and returns a string.
    (let [user (parse-user (slurp (:body request)))]
      (check-user-registered (:email user))
      (createUser user)
      ;; pipes operations
      (-> (response (json/generate-string user))
           (assoc :headers {"Content-Type" "application/json"})))
    (catch Exception e
      ;; ex-data gets the data from the exception (e)
      ;; ex-data returns a map with the keys :status and :errors
      ;; status is the status code and errors is the vector of errors
      ;; or is a default value of 400 if status is not set.
      (let [{:keys [status]} (ex-data e)]
        (-> (response (json/generate-string {:error (.getMessage e)}))
            (assoc :status (or status 400))
            (assoc :headers {"Content-Type" "application/json"}))))))
