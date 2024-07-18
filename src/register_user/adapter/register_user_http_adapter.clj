(ns register-user.adapter.register-user-http-adapter
  (:require [ring.util.response :refer [response]]
            [cheshire.core :as json]
            [clojure.string :as string]
            [toucan.db :as tdb]
            [infrastructure.db :as db]
            [register-user.register-user-use-case :refer [register-user, USER_ALREADY_REGISTERED_ERROR]]))

(def MISSING_ID_ERROR_MESSAGE
  "'id' must be a string")
(def MISSING_EMAIL_ERROR_MESSAGE
  "'email' must be a string")
(def MISSING_NAME_ERROR_MESSAGE
  "'name' must be a string")
(def INVALID_DATA_ERROR_MESSAGE
  "Invalid user data.")

(defn- add-content-type [data]
  (assoc data :headers {"Content-Type" "application/json"}))

(defn- parse-user
  [body]
  (let [user (json/parse-string body true)
        errors (cond-> []
                 (not (string? (:id user))) (conj MISSING_ID_ERROR_MESSAGE)
                 (not (string? (:email user))) (conj MISSING_EMAIL_ERROR_MESSAGE)
                 (not (string? (:name user))) (conj MISSING_NAME_ERROR_MESSAGE))]
    (if (empty? errors)
      user
      (throw (ex-info (str INVALID_DATA_ERROR_MESSAGE (string/join "; " errors)) {:status 400 :errors errors})))))

(defn ->RegisterUserHttpAdapter
  [user-repo] (fn [request]
                (try
                  (let [parsedUser (parse-user (slurp (:body request)))
                        createdUser (register-user user-repo parsedUser)
                        httpResponse (-> (response (json/generate-string createdUser))
                                         (add-content-type))]
                    httpResponse)
                  (catch Exception error
                    (let [httpErrorResponse (if (= USER_ALREADY_REGISTERED_ERROR (.getMessage error))
                                              (-> (response (json/generate-string {:error (.getMessage error)}))
                                                  (assoc :status 422)
                                                  (add-content-type))
                                              (-> (response (json/generate-string {:error (.getMessage error)}))
                                                  (assoc :status 400)
                                                  (add-content-type)))]
                      httpErrorResponse)))))

(defn ->RegisterUserHttpDbAdapter
  [user-repo] (fn [request]
                (try
                  (let [parsedUser (parse-user (slurp (:body request)))
                        createdUser ((tdb/insert! db/User parsedUser))
                        httpResponse (-> (response (json/generate-string createdUser))
                                         (add-content-type))]
                    httpResponse)
                  (catch Exception error
                    (let [httpErrorResponse (if (= USER_ALREADY_REGISTERED_ERROR (.getMessage error))
                                              (-> (response (json/generate-string {:error (.getMessage error)}))
                                                  (assoc :status 422)
                                                  (add-content-type))
                                              (-> (response (json/generate-string {:error (.getMessage error)}))
                                                  (assoc :status 400)
                                                  (add-content-type)))]
                      httpErrorResponse)))))

(defn create-register-user-http-adapter [user-repo]
  (->RegisterUserHttpDbAdapter user-repo))