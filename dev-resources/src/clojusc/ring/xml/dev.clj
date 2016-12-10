(ns clojusc.ring.xml.dev
  (:require [clojure.pprint :refer [print-table pprint]]
            [clojure.reflect :refer [reflect]]
            [clojure.string :as string]
            [clojure.walk :refer [macroexpand-all]]
            [clojusc.ring.xml :as xml]))

(defn show-methods
  ""
  [obj]
  (print-table
    (sort-by :name
      (filter :exception-types (:members (reflect obj))))))
