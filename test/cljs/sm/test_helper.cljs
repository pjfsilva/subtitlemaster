(ns sm.test_helper
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [sm.core :as sm]
            [cljs.core.async :as async]))

(def subdb-sandbox "http://sandbox.thesubdb.com/")
(def subdb-test-hash "edc1981d6459c6111fe36205b4aff6c2")

(defn fake-provider [res]
  (reify
    sm/SearchProvider
    (search-subtitles [_ path _]
      (assert (= path "test/fixtures/famous.mkv"))
      (go res))))

(defn failing-provider []
  (reify
    sm/SearchProvider
    (search-subtitles [_ _ _]
      (assert false "this should not have been called"))))

(defn fake-subtitle [result lang]
  (reify
    sm/Subtitle
    (download-stream [_] result)
    (subtitle-language [_] lang)))

(defn reduce-cat [c] (async/reduce conj [] c))