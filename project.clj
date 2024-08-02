(defproject hello-clojure "0.1.0-SNAPSHOT"
  :description "Learn Clojure."
  :url "https://github.com/dato-kontist/hello-clojure"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/core.match "1.0.0"]
                 [ring/ring-core "1.9.0"]
                 [ring/ring-mock "0.4.0"]
                 [clj-http "3.12.3"]
                 [org.clojure/data.json "2.4.0"]
                 [cheshire "5.13.0"]
                 [ring/ring-jetty-adapter "1.9.0"]
                 [org.postgresql/postgresql "42.2.19"]
                 [seancorfield/next.jdbc "1.2.659"]
                 [toucan "1.18.0"]
                 [mount "0.1.16"]
                 [ragtime "0.8.0"]]
  :main ^:skip-aot main
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
