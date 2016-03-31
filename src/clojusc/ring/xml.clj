(ns clojusc.ring.xml
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.data.xml :as xml]
            [clojure.zip :as zip]
            [ring.util.response :as ring])
  (:import [javax.xml.stream XMLStreamException]))

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

(defn coll->xml [data]
  "Convert a valid collection to XML."
  (-> data
      (xml/emit-str)
      (string/replace #"\n" "")))

(defn sexpr->xml [data]
  "Convert S-expression data to XML."
  (-> data
      (xml/sexp-as-element)
      (xml/emit-str)))

(defn ->xml [data options]
  "XML conversion dispatcher."
  (cond
    (:sexprs options) (sexpr->xml data)
    :else (coll->xml data)))

(defn xml->maps [str]
  "Parses an XML string and returns a vector of XML element maps."
  (xml/parse-str str))

(defn request->maps [request options]
  "Verifies an incoming request and consumes the body, parsing it and returning
  the result as an vector of XML element maps."
  (if-let [body (get-xml-body request)]
    (xml->maps body)))

(defn- xml-error-response [^Exception e]
  "Convert Exception into response"
  (-> (.getMessage e)
      (ring/response)
      (ring/status 400)
      (ring/content-type "text/plain")))

(defn wrap-xml-request
  "Intercepts incoming requests and attempts to parse the body as XML.

  If successful, will add the resulting XML maps to the :params key, the
  :xml-params key, and the :body."
  {:arglists '([handler] [handler options])}
  [handler & [{:as options}]]
  (fn [request]
    (if-let [xml-map (request->maps request options)]
      (handler (-> request
                   (assoc :body xml-map)
                   (assoc :xml-params xml-map)
                   (update-in [:params] merge xml-map)))
      (handler request))))

(defn wrap-xml-response
  "Intercepts outgoing collections and attempts to coerce them into XML.

  Accepts the following options:

  :sexprs   - true if the body is expected to be in the form of vector
              S-expresions
  :elements - true ot the body is expected to be in the form of a vector
              of XML maps
  "
  {:arglists '([handler] [handler options])}
  [handler & [{:as options}]]
  (fn [request]
    (let [response (handler request)]
      (if (coll? (:body response))
        (let [xml-response (update-in response [:body] ->xml options)]
          (if (contains? (:headers response) "Content-Type")
            xml-response
            (ring/content-type xml-response "application/xml; charset=utf-8")))
        response))))
