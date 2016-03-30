(ns clojusc.ring.test.xml
  (:use clojusc.ring.xml
        clojure.test
        ring.util.io))

(def request-1 {:content-type "application/xml"
                :body (string-input-stream "<xml></xml>")})

(def request-2 {:content-type "text/xml"
                :body (string-input-stream "<xml></xml>")})

(def request-3 {:content-type "application/xml"
                :body (string-input-stream "<xml></XML>")})

(def request-4 {:content-type "text/plain"
                :body (string-input-stream "stuff")})

(deftest test-xml-request?
  (is (xml-request? request-1))
  (is (xml-request? request-2))
  (is (not (xml-request? request-4))))

(deftest test-xml-request-body
  (let [handler (wrap-xml-request identity)]
    (testing "xml body"
      (let [response  (handler request-1)]
        (is (= [{:tag :xml :attrs nil :content nil} nil] (:body response)))))))

(deftest test-xml-response
  (testing "properly formatted vector/map body"
    (let [handler (constantly {:status 200 :headers {}
                               :body [{:tag :xml :attrs nil :content nil} nil]})
          response ((wrap-xml-response handler) {})]
      (is (= (get-in response [:headers "Content-Type"]) "application/xml; charset=utf-8"))
      (is (= (:body response) "<xml/>")))))

(deftest test-invalid-xml-returns-400
  (let [handler (wrap-xml-request identity)
        response  (handler request-3)]
    (is (= (:status response) 400))
    (is (= (get-in response [:headers "Content-Type"]) "text/plain"))
    (is (= (:body response) "The element type \"xml\" must be terminated by the matching end-tag \"</xml>\"."))))
