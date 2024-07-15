(ns hello-clojure.web-core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.util.response :refer [response]]))

(defn handler [_]
  (response "Hello, World!"))

(defn -main []
  (run-jetty handler {:port 3000}))