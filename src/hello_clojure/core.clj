(ns hello-clojure.core
  (:gen-class))

(defn- printHello ^clojure.lang.IFn [args]
  (if (empty? args)
    (fn [] (println "Hello, World!"))
    (fn [] (println (str "Hello, " (first args) "!")))))

(defn -main
  [& args]
  (let [printHandler (printHello args)]
    (printHandler)))