(ns main
  (:require [register-user.register-user :refer [registerUserHttpHandler]]
            [ring.adapter.jetty :refer [run-jetty]]))

(defn -main []
  (run-jetty registerUserHttpHandler {:port 3000}))