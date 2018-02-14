(ns handler.core
  (:require [cljs.nodejs]
            [cljs.core.async :refer [chan]]
            [taoensso.timbre :as timbre
             :refer (log  trace  debug  info  warn  error  fatal  report
                     logf tracef debugf infof warnf errorf fatalf reportf
                     spy get-env log-env)]
            [oops.core :refer [oget oset! ocall oapply ocall! oapply!
                               oget+ oset!+ ocall+ oapply+ ocall!+ oapply!+]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

; default logging level is :debug
(timbre/set-level! :trace)

(def operators {"+" +
                "-" -
                "*" *
                "/" /})

(defn extract-query-parameter
  "Extracts query parameter from req. Returns nil if querystring or parameter is empty"
  ([req parameter default]
   (let [querystring (cljs.nodejs/require "querystring")
         query-string (get req :query-string)]
       (cond
         (nil? query-string) default
         :else
         (goog.object/get (.parse querystring query-string) parameter default))))
  ([req parameter]
   (extract-query-parameter req parameter nil)))

(defn calc
  [req]
  (let [operator (extract-query-parameter req "op" nil)
        value1 (-> (extract-query-parameter req "value1" nil) js/parseInt)
        value2 (-> (extract-query-parameter req "value2" nil) js/parseInt)]
    (cond
      (or (nil? operator) (= js/NaN value1) (= js/NaN value2))
      (throw (js/Error. "Missing input parameter!"))
      (contains? operators operator)
      (str ((get operators operator) value1 value2))
      :else
      (throw (js/Error. "Bad operator type!")))))

(defn log-hello
  []
  (info "Hello, Timbre!"))

(defn hello
  [req]
  (log-hello)
  (debug timbre/*config*)
  "hello!")

(defn hello-json
  [req]
  (info "queue triggered function")
  {:body {:key1 "value1" :key2 5}})

(defn fail-hard
  [req]
  (go
    (throw (js/Error. "Fail hard! Hedge does not catch this? And AWS/Azure does not handle this properly"))
    "Fake return value which should be handled by hedge."))
