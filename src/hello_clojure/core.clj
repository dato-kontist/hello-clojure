;; This is a namespace declaration. It defines the namespace of the current file.
;; The namespace is named hello-clojure.core.
;; The :gen-class directive tells Clojure that this namespace should be compiled to a Java class.
(ns hello-clojure.core
  (:gen-class))

(defn- printHello [args]
  (if (empty? args)
    ;; if true
    (println "Hello, World!")
    ;; "else?" false
    (println (str "Hello, " (first args) "!"))))

;; This is a function definition. The -main function takes an optional list of arguments.
(defn -main
  ;; This is a vector of arguments. The & indicates that the function can take any number of arguments.
  [& args]
  ;; This is a function call.
  (printHello args))