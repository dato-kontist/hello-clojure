(ns infrastructure.docker
  (:require [mount.core :refer [defstate]]
            [clojure.java.shell :refer [sh]]
            [clojure.tools.logging :as log]))

(def postgres-user (or (System/getenv "POSTGRES_USER") "user"))
(def postgres-db (or (System/getenv "POSTGRES_DB") "mydb"))

(defn start-docker-postgres []
  (sh "docker-compose" "up" "-d" "db"))

(defn stop-docker-postgres []
  (sh "docker-compose" "down"))

(defn db-ready? []
  (let [result (sh "docker-compose" "exec" "-T" "db" "pg_isready" "-U" postgres-user "-d" postgres-db)]
    (zero? (:exit result))))

(defn wait-for-db [timeout-seconds]
  (let [start (System/currentTimeMillis)]
    (loop []
      (when (and (< (- (System/currentTimeMillis) start) (* timeout-seconds 1000))
                 (not (db-ready?)))
        (Thread/sleep 1000)
        (recur))))
  (if (db-ready?)
    (log/info "Database is ready.")
    (throw (Exception. "Database did not become ready in time."))))

#_{:clj-kondo/ignore [:unresolved-symbol]}
(defstate docker-postgres
  :start (do
           (start-docker-postgres)
           (wait-for-db 60))
  :stop (stop-docker-postgres))