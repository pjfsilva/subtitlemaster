(ns wilkerdev.util.reactive-test
  (:require-macros [wilkerdev.util.macros :refer [<? test dochan]]
                   [cljs.core.async.macros :refer [go]])
  (:require [wilkerdev.util.reactive :as r]
            [sm.test-helper :refer [alternate-calls]]
            [cljs.core.async :refer [<! timeout alts! put! chan close!]]))

(defn counting [f]
  (let [counter (atom 0)]
    [(fn [& args]
       (swap! counter inc)
       (apply f args))
     counter]))

(test "async memorize"
  (let [[f counter] (counting (fn [x] (go x)))
        memoized (r/memoize-async f)]
    (assert (= 1 (<! (memoized 1))))
    (assert (= 1 (<! (memoized 1))))
    (assert (= 5 (<! (memoized 5))))
    (assert (= 5 (<! (memoized 5))))
    (assert (= 5 (<! (memoized 5))))
    (assert (= 2 @counter))))

(test "channel pool"
  (let [pool (r/channel-pool 1)
        oc1 (chan)
        oc2 (chan)
        ec1 (r/pool-enqueue pool (constantly oc1))
        ec2 (r/pool-enqueue pool (constantly oc2))]
    (put! oc2 2)
    (put! oc1 1)
    (close! oc1)
    (assert (= 1 (<! ec1)))
    (assert (= 2 (<! ec2)))))

(test "reduce"
  (let [reduce-fn (fn [x y] (go (+ x y)))
        res (<! (r/reduce reduce-fn 0 (r/spool [1 2 3 4])))]
    (assert (= 10 res))))

(test "retry"
  (let [f (fn [] (go :result))]
    (assert (= :result (<? (r/retry f 5))))))

(test "retry - multi"
  (let [err (js/Error. "failed")]
    (assert (= :ok (<? (r/retry (alternate-calls err err :ok) 2))))
    (assert (= err (<! (r/retry (alternate-calls err :ok) 0))))))
