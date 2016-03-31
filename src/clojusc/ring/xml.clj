(ns clojusc.ring.xml
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [ring.util.response :as ring])
  (:import [org.xml.sax SAXParseException]))

(defn xml-request?
  "Determine if the incoming collection represents an XML request."
  [request]
  (if-let [type (:content-type request)]
    (not (empty? (re-find #"(.*/xml)" type)))))

(defn get-xml-body
  "Get the XML body from a request."
  [request]
  (if (xml-request? request)
    (if-let [body (:body request)]
      (if-not (coll? body)
        body))
    nil))

(defn ->xml [data]
  "Convert a valid collection to XML."
  (-> data
      (zip/root)
      (xml/emit-element)
      (with-out-str)
      (string/replace #"\n" "")))

(defn ->maps [str]
  "Parses a string as XML and returns a vector of XML elements as maps."
  (-> str
      (io/input-stream)
      (xml/parse)
      (zip/xml-zip)))

(defn request->maps [request]
  "Verifies an incoming request and consumes the body, parsing it and returning
  the result as an vector of XML elements as maps."
  (if-let [body (get-xml-body request)]
    (->maps body)))

(defn- xml-error-response [^Exception e]
  "Convert Exception into response"
  (-> (.getMessage e)
      (ring/response)
      (ring/status 400)
      (ring/content-type "text/plain")))

(defn wrap-xml-request [handler]
  "Intercepts incoming requests and attempts to parse the body as XML. If
  successful, will add the resulting XML maps to the :params key, the :xml-params
  key, and the :body."
  (fn [request]
    (try
      (if-let [xml-map (request->maps request)]
        (handler (-> request
                     (assoc :body xml-map)
                     (assoc :xml-params xml-map)
                     (update-in [:params] merge xml-map)))
        (handler request))
      (catch SAXParseException spe (xml-error-response spe)))))

(defn wrap-xml-response [handler]
  "Intercepts outgoing collections and attempts to coerce them into XML."
  (fn [request]
    (let [response (handler request)]
      (if (coll? (:body response))
        (let [xml-response (update-in response [:body] ->xml)]
          (if (contains? (:headers response) "Content-Type")
            xml-response
            (ring/content-type xml-response "application/xml; charset=utf-8")))
        response))))
