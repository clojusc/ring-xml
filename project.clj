(defproject clojusc/ring-xml "0.2.0-SNAPSHOT"
  :description "Ring middleware for XML requests and responses."
  :url "https://github.com/clojusc/ring-xml"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/data.xml "0.1.0-beta1"]
                 [ring/ring-core "1.6.0-beta6"]]
   :profiles {
    :uber {
      :aot :all}
    :test {
      :plugins [
        [jonase/eastwood "0.2.3" :exclusions [org.clojure/clojure]]
        [lein-kibit "0.1.3"]
        [lein-midje "3.2.1" :exclusions [midje]]]}
    :1.5 {
      :dependencies [
        [org.clojure/clojure "1.5.0"]
        [medley "0.6.0" :exclusions [org.clojure/clojure]]]}
    :1.6 {
      :dependencies [
        [org.clojure/clojure "1.6.0"]
        [medley "0.6.0" :exclusions [org.clojure/clojure]]]}
    :1.7 {
      :dependencies [
        [org.clojure/clojure "1.7.0"]]}
    :1.8 {
      :dependencies [
        [org.clojure/clojure "1.8.0"]]}
    :1.9 {
      :dependencies [
        [org.clojure/clojure "1.9.0-alpha14"]]}
    :dev {
      :source-paths ["dev-resources/src"]
      :repl-options {:init-ns clojusc.ring.xml}
      :dependencies [
        [org.clojure/tools.namespace "0.2.11"
          :exclusions [org.clojure/clojure]]]}})
