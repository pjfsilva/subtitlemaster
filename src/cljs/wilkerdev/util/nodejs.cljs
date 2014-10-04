(ns wilkerdev.util.nodejs
  (:require-macros [wilkerdev.util.macros :refer [<? go-catch]])
  (:require [cljs.core.async :refer [chan put! close!]]
            [wilkerdev.util.reactive]))

(def fs (js/require "fs"))
(def node-request (js/require "request"))
(def crypto (js/require "crypto"))
(def xmlrpc (js/require "xmlrpc"))

(defn make-js-error [node-err]
  (.log js/console "node err" node-err)
  (if (instance? js/Error node-err)
    node-err
    (js/Error. (.-message node-err))))

(defn node-callback [c]
  (fn [err res]
    (if-not err
      (put! c (or res :done))
      (put! c (make-js-error err)))
    (close! c)))

(defn node->chan* [f & args]
  (let [c (chan 1)
        args (conj (vec args) (node-callback c))
        res (apply f args)]
    [c res]))

(defn node->chan [& args]
  (nth (apply node->chan* args) 0))

(defn node-lift [f]
  (fn [& args]
    (go-catch
      (<? (apply node->chan f args)))))

(def rename (node-lift (.-rename fs)))
(def mkdir (node-lift (.-mkdir fs)))
(def lstat (node-lift (.-lstat fs)))
(def fopen (node-lift (.-open fs)))
(def fread (node-lift (.-read fs)))
(def read-file (node-lift (.-readFile fs)))

(defn create-read-stream [path] (.createReadStream fs path))

(defn http [options]
  (go-catch
    (<? (node->chan node-request (clj->js options)))))

(defn http-post-form [options builder]
  (let [[c req] (node->chan* node-request (clj->js (merge options {:method "POST"
                                                                   :postambleCRLF true})))]
    (builder (.form req))
    c))

(defn md5-hex [buffer]
  (let [sum (.createHash crypto "md5")]
    (.update sum buffer)
    (.digest sum "hex")))

(defn xmlrpc-client [options]
  (.createClient xmlrpc (clj->js options)))

(defn xmlrpc-call [client method & args]
  (let [c (chan 1)]
    (.methodCall client method (clj->js args) (node-callback c))
    c))
