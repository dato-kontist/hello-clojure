(ns infrastructure.db
  (:require [toucan.db :as db]
            [toucan.models :refer [defmodel]]
            [mount.core :refer [defstate]]
            [clojure.edn :as edn]
            [infrastructure.migrations :as migrations]
            [next.jdbc :as jdbc]))

(defn load-config []
  (edn/read-string (slurp "resources/config.edn")))

#_{:clj-kondo/ignore [:unresolved-symbol]}
(defstate db-connection
  :start (let [{:keys [db]} (load-config)]
           (let [datasource (jdbc/get-datasource db)]
             (db/set-default-db-connection! {:datasource datasource})
             (migrations/migrate)
             datasource))
  :stop (when-let [datasource (:datasource db-connection)]
          (.close datasource)))

#_{:clj-kondo/ignore [:unresolved-symbol]}
(defmodel User :users)