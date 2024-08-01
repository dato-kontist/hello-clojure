(ns main
  (:require [register-user.adapter.register-user-http-adapter :refer [create-register-user-http-adapter]]
            [ring.adapter.jetty :refer [run-jetty]]
            [mount.core :as mount]
            [clojure.tools.logging :as log]
            [infrastructure.db :refer [db-connection]]
            [infrastructure.docker :refer [docker-postgres]]
            [register-user.adapter.in-memory-user-repository :refer [create-in-memory-repository]]
            [register-user.adapter.psql-user-repository :refer [create-in-psql-user-repository]]
            [ring.middleware.params :as params]
            [ring.middleware.keyword-params :as keyword-params]
            [ring.middleware.resource :as resource]
            [ring.util.response :as response]))

(def in-memory-user-db (atom {}))

(def in-memory-user-repo (create-in-memory-repository in-memory-user-db))
(def psql-user-repo (create-in-psql-user-repository db-connection))

(def app
  (-> (fn [request]
        (case (:uri request)
          "/users/in-memory" ((create-register-user-http-adapter in-memory-user-repo) request)
          "/users/psql" ((create-register-user-http-adapter psql-user-repo) request)
          (response/not-found "Not Found")))
      (params/wrap-params)
      (keyword-params/wrap-keyword-params)
      (resource/wrap-resource "/")))

(defn -main [& {:keys [join?] :or {join? true}}]
  (try
    (log/info "Starting Docker Postgres...")
    (mount/start #'docker-postgres)
    (log/info "Docker Postgres started.")
    (log/info "Starting DB connection...")
    (mount/start #'db-connection)
    (log/info "DB connection started.")
    (log/info "Starting Jetty server...")
    (run-jetty app {:port 3000 :join? join?})

    (log/info "Jetty server started.")
    (catch Exception e
      (log/error e "Failed to start the application."))))