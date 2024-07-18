(ns infrastructure.docker
  (:require [mount.core :refer [defstate]]
            [clojure.java.shell :refer [sh]]))

(defn start-docker-postgres []
  (sh "docker-compose" "up" "-d" "db"))

(defn stop-docker-postgres []
  (sh "docker-compose" "down"))

#_{:clj-kondo/ignore [:unresolved-symbol]}
(defstate docker-postgres
  :start (start-docker-postgres)
  :stop (stop-docker-postgres))