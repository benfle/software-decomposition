(ns user
  (:require [clojure.java.io :refer [resource]]
            [kwic.module11 :as m11]
            [kwic.module21 :as m21]
            [kwic.module22 :as m22]
            [kwic.module23 :as m23]
            [clojure.pprint :refer [pprint]]
            [clojure.string :as str]))

(def txt (resource "constitution"))

(defn show-mem-used []
  (let [runtime (Runtime/getRuntime)]
    (.gc runtime)
    (printf "Total Memory: %,d\n" (.totalMemory runtime))))

(defn kwic-index
  [stream]
  (->> stream
       slurp
       str/split-lines
       (map #(str/split % #" "))
       (mapcat (fn [line]
                 (map #(->> (split-at % line)
                            reverse
                            (apply concat))
                      (range (count line)))))
       (sort-by #(str/upper-case (first %)))))
