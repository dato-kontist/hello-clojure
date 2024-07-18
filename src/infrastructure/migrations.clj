(ns infrastructure.migrations
  (:require
   [ragtime.repl :as ragtime]
   [clojure.edn :as edn]))

(defn load-config []
  (edn/read-string (slurp "resources/migrations.edn")))

(defn migrate []
  (let [config (load-config)]
    (ragtime/migrate config)))

(defn rollback []
  (let [config (load-config)]
    (ragtime/rollback config)))