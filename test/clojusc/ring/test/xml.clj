(ns clojusc.ring.test.xml
  (:require [clojure.test :refer [deftest testing is]]
            [clojure.data.xml :as xml]
            [ring.util.io :as io]
            [clojusc.ring.xml :as ring-xml]))

(def empty-xml (xml/element :xml))
(def data-xml (xml/element :xml nil (xml/element :data nil "stuff")))

(def sexpr-empty-xml [:xml])
(def sexpr-data-xml [:xml [:data "stuff"]])

(def prefix "<?xml version=\"1.0\" encoding=\"UTF-8\"?>")

(def str-empty-xml (str prefix "<xml></xml>"))
(def str-data-xml (str prefix "<xml><data>stuff</data></xml>"))
(def str-bad-tags-xml (str prefix "<foo></bar>"))

(def request-1 {:content-type "application/xml"
                :body str-empty-xml})

(def request-2 {:content-type "text/xml"
                :body str-empty-xml})

(def request-3 {:content-type "text/xml"
                :body str-bad-tags-xml})

(def request-4 {:content-type "text/plain"
                :body "stuff"})

(deftest test-coll->xml
  (is (= str-empty-xml (ring-xml/coll->xml empty-xml)))
  (is (= str-data-xml (ring-xml/coll->xml data-xml))))

(deftest test-sexpr->xml
  (is (= str-empty-xml (ring-xml/sexpr->xml sexpr-empty-xml)))
  (is (= str-data-xml (ring-xml/sexpr->xml sexpr-data-xml))))

(deftest test-xml->maps
  (is (= empty-xml (ring-xml/xml->maps str-empty-xml)))
  (is (= data-xml (ring-xml/xml->maps str-data-xml))))

(deftest test-xml-request?
  (is (ring-xml/xml-request? request-1))
  (is (ring-xml/xml-request? request-2))
  (is (not (ring-xml/xml-request? request-4))))

(deftest test-xml-request-body
  (let [handler (ring-xml/wrap-xml-request identity)]
    (testing "XML Body"
      (let [response (handler request-1)]
        (is (= empty-xml (:body response)))))))

(deftest test-xml-response
  (testing "No options"
    (let [handler (constantly {:status 200 :headers {}
                               :body empty-xml})
          response ((ring-xml/wrap-xml-response handler) {})]
      (is (= (get-in response [:headers "Content-Type"]) "application/xml; charset=utf-8"))
      (is (= (:body response) str-empty-xml))))
  (testing "Element option"
    (let [handler (constantly {:status 200 :headers {}
                               :body empty-xml})
          response ((ring-xml/wrap-xml-response handler {:elements true}) {})]
      (is (= (get-in response [:headers "Content-Type"]) "application/xml; charset=utf-8"))
      (is (= (:body response) str-empty-xml))))
  (testing "Element option"
    (let [handler (constantly {:status 200 :headers {}
                               :body sexpr-empty-xml})
          response ((ring-xml/wrap-xml-response handler {:sexprs true}) {})]
      (is (= (get-in response [:headers "Content-Type"]) "application/xml; charset=utf-8"))
      (is (= (:body response) str-empty-xml)))))
