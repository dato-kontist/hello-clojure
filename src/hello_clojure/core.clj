;; This is a namespace declaration. It defines the namespace of the current file.
;; The namespace is named hello-clojure.core.
;; The :gen-class directive tells Clojure that this namespace should be compiled to a Java class.
(ns hello-clojure.core
  (:gen-class))

;; This is a function definition. The -main function takes an optional list of arguments.
(defn -main
  ;; This is a docstring. It describes what the function does. (I wonder why should one clean code if they can explain all what the code does in documentation - irony)
  "Prints a greeting message. Optionally takes a name as an argument."
  ;; This is a vector of arguments. The & indicates that the function can take any number of arguments.
  [& args]
  ;; This is an if statement. It checks if the args vector is empty.
  (if (empty? args)
    ;; if true
    (println "Hello, World!")
    ;; "else?" false
    (println (str "Hello, " (first args) "!"))))