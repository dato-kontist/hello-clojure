(ns hello-clojure.core
  (:gen-class))

(defn -main
  "Prints a greeting message. Optionally takes a name as an argument."
  [& args]
  (if (empty? args)
    (println "Hello, World!")
    (println (str "Hello, " (first args) "!"))))