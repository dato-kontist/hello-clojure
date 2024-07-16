(ns register-user.register-user
  (:require
   [ring.util.response :refer [response]]
   [cheshire.core :as json]
   [register-user.use-case :refer [register-user]]))

(defn- parse-user
  "Parses the user data from the request body."
  [body]
  (let [user (json/parse-string body true)
        errors (cond-> []
                 (not (string? (:id user))) (conj "id must be a string")
                 (not (string? (:email user))) (conj "email must be a string")
                 (not (string? (:name user))) (conj "name must be a string"))]
    (if (empty? errors)
      user
      (throw (ex-info "Invalid user data" {:status 400 :errors errors})))))

(defn registerUserHttpHandler
  "Handles the HTTP request to register a user."
  [request]
  (try
    (let [user (parse-user (slurp (:body request)))
          createdUser (register-user user)]
      (-> (response (json/generate-string createdUser))
          (assoc :headers {"Content-Type" "application/json"})))
    (catch Exception e
      (let [{:keys [status]} (ex-data e)]
        (-> (response (json/generate-string {:error (.getMessage e)}))
            (assoc :status (or status 400))
            (assoc :headers {"Content-Type" "application/json"}))))))