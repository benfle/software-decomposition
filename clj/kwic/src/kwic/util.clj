(ns kwic.util
  (:require [clojure.java.io :refer [reader writer]]
            [clojure.string :as str]))

(defn indexes-of
  "Returns a lazy seq of the indexes of `s` in `txt`."
  ([txt s]
   (indexes-of txt s 0))
  ([txt s start-from]
   (indexes-of txt s start-from nil))
  ([txt s start-from stop-at]
   (lazy-seq
    (let [idx (.indexOf txt s start-from)]
      (when (and (or (nil? stop-at) (< idx stop-at))
                 (not= -1 idx))
        (cons idx (indexes-of txt s (inc idx) stop-at)))))))
