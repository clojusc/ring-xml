(defproject clojusc/ring-xml "0.2.0-SNAPSHOT"
  :description "Ring middleware for XML requests and responses."
  :url "https://github.com/clojusc/ring-xml"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.xml "0.1.0-beta1"]
                 [ring/ring-core "1.6.0-beta6"]]
  :repl-options {:init-ns clojusc.ring.xml})
