(ns clojusc.ring.test.xml
  (:require [clojure.test :refer [deftest testing is]]
            [ring.util.io :as io]
            [clojusc.ring.xml :as ring-xml]))

(def empty-xml-self-closing [{:tag :xml} nil])
(def empty-xml-self-closing-expanded [{:tag :xml, :attrs nil, :content nil} nil])
(def empty-xml [{:tag :xml :content []} nil])
(def data-xml [{:tag :xml, :content [{:tag :data, :content ["stuff"]}]} nil])

(def str-empty-xml-self-closing "<xml/>")
(def str-empty-xml "<xml></xml>")
(def str-empty-xml-mixed-case "<xml></XML>")
(def str-data-xml "<xml><data>stuff</data></xml>")

(def request-1 {:content-type "application/xml"
                :body (io/string-input-stream str-empty-xml)})

(def request-2 {:content-type "text/xml"
                :body (io/string-input-stream str-empty-xml)})

(def request-3 {:content-type "application/xml"
                :body (io/string-input-stream str-empty-xml-mixed-case)})

(def request-4 {:content-type "text/plain"
                :body (io/string-input-stream "stuff")})

(deftest test->xml
  (is (= str-empty-xml-self-closing (ring-xml/->xml empty-xml-self-closing)))
  (is (= str-empty-xml (ring-xml/->xml empty-xml)))
  (is (= str-data-xml (ring-xml/->xml data-xml))))

(deftest test->maps
  (is (= [{:tag :xml, :attrs nil, :content nil} nil]
         (ring-xml/->maps (io/string-input-stream str-empty-xml-self-closing))))
  (is (= [{:tag :xml, :attrs nil, :content nil} nil]
         (ring-xml/->maps (io/string-input-stream str-empty-xml))))
  (is (= [{:tag :xml, :attrs nil, :content [{:tag :data, :attrs nil, :content ["stuff"]}]} nil]
         (ring-xml/->maps (io/string-input-stream str-data-xml)))))

(deftest test-xml-request?
  (is (ring-xml/xml-request? request-1))
  (is (ring-xml/xml-request? request-2))
  (is (not (ring-xml/xml-request? request-4))))

(deftest test-xml-request-body
  (let [handler (ring-xml/wrap-xml-request identity)]
    (testing "XML Body"
      (let [response (handler request-1)]
        (is (= empty-xml-self-closing-expanded (:body response)))))))

(deftest test-xml-response
  (testing "Properly formatted vector/map body"
    (let [handler (constantly {:status 200 :headers {}
                               :body empty-xml-self-closing})
          response ((ring-xml/wrap-xml-response handler) {})]
      (is (= (get-in response [:headers "Content-Type"]) "application/xml; charset=utf-8"))
      (is (= (:body response) str-empty-xml-self-closing)))))

(deftest test-invalid-xml-returns-400
  (let [handler (ring-xml/wrap-xml-request identity)
        response  (handler request-3)]
    (is (= (:status response) 400))
    (is (= (get-in response [:headers "Content-Type"]) "text/plain"))
    (is (= (:body response) "The element type \"xml\" must be terminated by the matching end-tag \"</xml>\"."))))
