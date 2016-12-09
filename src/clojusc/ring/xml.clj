(ns clojusc.ring.xml
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.data.xml :as xml]
            [ring.util.response :as ring])
  (:import [javax.xml.stream XMLStreamException]
           [java.io InputStream]))

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
      (cond
       (instance? InputStream body) (slurp body)
       (not (coll? body)) body))
    nil))

(defn ->xml [data options]
  "XML conversion dispatcher."
  (xml/emit-str
    (cond
      (:sexprs options) (xml/sexp-as-element data)
      :else data)))

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
  :elements - true if the body is expected to be in the form of a vector
              of XML maps

  For backwards-compatibility reasons the wrapper assumes that
  clojure.data.xml elements are provided; as such the default behaviour
  is as if the options {:elements true} are passed to this wrapper.
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
