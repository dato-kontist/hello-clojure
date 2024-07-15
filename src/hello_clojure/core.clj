(ns hello-clojure.core
  (:gen-class))

;; ^clojure.lang.IFn is a type hint for the return type of the function. Although not enforced as it is a dynamic typed language to be encoded into Java
;; defn-: Defines a private function. This function is only accessible within the namespace where it is defined.
(defn- printHello ^clojure.lang.IFn [args]
  (if (empty? args)
    (fn [] (println "Hello, World!"))
    (fn [] (println (str "Hello, " (first args) "!")))))

;; defn: Defines a public function. This function can be accessed from other namespaces.
(defn -main
  [& args]
  (let [printHandler (printHello args)]
    (printHandler)))