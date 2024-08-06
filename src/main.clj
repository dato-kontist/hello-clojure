(ns main
  (:require
   [interfaces.rest.register-user-http-adapter :refer [create-register-user-http-adapter]]
   [interfaces.rest.find-user-by-email-http-adapter :refer [create-find-user-by-email-http-adapter]]
   [interfaces.rest.delete-user-http-adapter :refer [create-delete-user-http-adapter]]
   [ring.adapter.jetty :refer [run-jetty]]
   [mount.core :as mount]
   [clojure.tools.logging :as log]
   [infrastructure.db :refer [db-connection]]
   [infrastructure.docker :refer [docker-postgres]]
   [infrastructure.repositories.in-memory-user-repository :refer [create-in-memory-repository]]
   [infrastructure.repositories.psql-user-repository :refer [create-in-psql-user-repository]]
   [ring.middleware.params :as params]
   [ring.middleware.keyword-params :as keyword-params]
   [ring.middleware.resource :as resource]
   [ring.util.response :as response]
   [clojure.tools.logging :as logger]))

(def in-memory-user-db (atom {}))

(def in-memory-user-repo (create-in-memory-repository in-memory-user-db))
(def psql-user-repo (create-in-psql-user-repository db-connection))

(def app
  (-> (fn [request]
        (let [uri (:uri request)
              method (:request-method request)]
          (logger/info uri)
          (logger/info method)
          (case [uri method]
            ["/users/in-memory" :post] ((create-register-user-http-adapter in-memory-user-repo) request)
            ["/users/psql" :post] ((create-register-user-http-adapter psql-user-repo) request)
            ["/users/in-memory" :get] ((create-find-user-by-email-http-adapter in-memory-user-repo) request)
            ["/users/psql" :get] ((create-find-user-by-email-http-adapter psql-user-repo) request)
            ["/users/in-memory" :delete] ((create-delete-user-http-adapter in-memory-user-repo) request)
            ["/users/psql" :delete] ((create-delete-user-http-adapter psql-user-repo) request)
            (response/not-found "Not Found"))))
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