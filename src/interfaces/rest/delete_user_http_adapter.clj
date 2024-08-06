(ns interfaces.rest.delete-user-http-adapter
  (:require [ring.util.response :refer [response]]
            [cheshire.core :as json]
            [application.commands.delete-user-use-case :refer [delete-user-by-id USER_NOT_FOUND_ERROR]]))

(def MISSING_ID_ERROR_MESSAGE
  "'id' must be present and a valid string")

(defn- add-content-type [data]
  (assoc data :headers {"Content-Type" "application/json"}))

(defn- validate-id [id]
  (if (not (string? id))
    (throw (ex-info MISSING_ID_ERROR_MESSAGE {:status 400 :error MISSING_ID_ERROR_MESSAGE}))
    id))

(defn ->DeleteUserHttpAdapter
  [user-repo] (fn [request]
                (try
                  (let [id (validate-id (get-in request [:query-params "id"]))
                        _ (delete-user-by-id user-repo id)
                        httpResponse (-> (response (json/generate-string {:message "User deleted successfully"}))
                                         (add-content-type)
                                         (assoc :status 200))]
                    httpResponse)
                  (catch Exception error
                    (let [httpErrorResponse (cond
                                              (= (ex-message error) USER_NOT_FOUND_ERROR)
                                              (-> (response (json/generate-string {:error "User not found."}))
                                                  (assoc :status 422)
                                                  (add-content-type))
                                              (= (ex-message error) MISSING_ID_ERROR_MESSAGE)
                                              (-> (response (json/generate-string {:error MISSING_ID_ERROR_MESSAGE}))
                                                  (assoc :status 400)
                                                  (add-content-type))
                                              :else
                                              (->
                                               (response (json/generate-string {:error "Internal Server Error"}))
                                               (assoc :status 500)
                                               (add-content-type)))]
                      httpErrorResponse)))))

(defn create-delete-user-http-adapter [user-repo]
  (->DeleteUserHttpAdapter user-repo))