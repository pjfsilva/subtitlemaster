(ns wilkerdev.util
  (:require [camel-snake-kebab.core :as csk]))

(defn js->map [obj]
  (->> (js-keys obj)
       (map #(vector % (aget obj %)))
       (map #(update-in % [0] (comp keyword csk/->kebab-case)))
       (into {})))
