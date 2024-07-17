(ns main
  (:require [register-user.register-user :refer [registerUserHttpHandler]]
            [ring.adapter.jetty :refer [run-jetty]]
            [register-user.adapter.in-memory-user-repository :refer [create-in-memory-repository]]))

(def in-memory-user-db (atom {}))

(def in-memory-user-repo (create-in-memory-repository in-memory-user-db))

(defn -main []
  (run-jetty (registerUserHttpHandler in-memory-user-repo) {:port 3000}))