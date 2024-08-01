(ns main
  (:require [register-user.adapter.register-user-http-adapter :refer [create-register-user-http-adapter]]
            [ring.adapter.jetty :refer [run-jetty]]
            [register-user.adapter.in-memory-user-repository :refer [create-in-memory-repository]]))

(def in-memory-user-db (atom {}))

(def in-memory-user-repo (create-in-memory-repository in-memory-user-db))

(defn -main [& {:keys [join?] :or {join? true}}]
  (run-jetty (create-register-user-http-adapter in-memory-user-repo) {:port 3000 :join? join?}))