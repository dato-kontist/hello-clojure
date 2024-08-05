(ns interfaces.rest.find-user-by-email-http-adapter
  (:require [ring.util.response :refer [response]]
            [cheshire.core :as json]
            [application.queries.find-user-by-email-use-case :refer [find-user-by-email USER_NOT_FOUND]]))

(defn- add-content-type [resp]
  (assoc-in resp [:headers "Content-Type"] "application/json"))

(defn- validate-email [email]
  (if (not (string? email))
    "'email' must be a string"
    nil))

(defn ->FindUserByEmailHttpAdapter
  [user-repo]
  (fn [request]
    (try
      (let [email (get-in request [:query-params "email"])
            error (validate-email email)]
        (if error
          (-> (response (json/generate-string {:error error}))
              (assoc :status 400)
              add-content-type)
          (let [user (find-user-by-email user-repo email)]
            (if user
              (-> (response (json/generate-string user))
                  (assoc :status 200)
                  add-content-type)
              (-> (response (json/generate-string {:error USER_NOT_FOUND}))
                  (assoc :status 404)
                  add-content-type)))))
      (catch Exception e
        (let [msg (.getMessage e)]
          (cond
            (= msg USER_NOT_FOUND)
            (-> (response (json/generate-string {:error msg}))
                (assoc :status 404)
                add-content-type)
            :else
            (-> (response (json/generate-string {:error "Internal Server Error"}))
                (assoc :status 500)
                add-content-type)))))))

(defn create-find-user-by-email-http-adapter [user-repo]
  (->FindUserByEmailHttpAdapter user-repo))