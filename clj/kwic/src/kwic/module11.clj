(ns kwic.module11
  (:require [clojure.string :as str]
            [kwic.util :refer [indexes-of]]
            [clojure.java.io :refer [reader writer]])
  (:import [java.io StringWriter]
           [java.io LineNumberReader]))

(defn read-lines
  [input]
  (->> input
       slurp
       str/split-lines
       (remove str/blank?)
       (map #(into [] (str/split % #" ")))
       (into [])))

(defn circular-shifts
  [lines]
  (->> lines
       (map-indexed (fn [line-idx words]
                      (map (fn [word-idx]
                             [line-idx word-idx])
                           (range (count words)))))
       (apply concat)))

(defn alphabetize
  [lines shifts]
  (sort-by #(str/lower-case (get-in lines %))
           shifts))

(defn output
  [lines sorted-shifts writer]
  (doseq [[line-idx word-idx] sorted-shifts]
    (.write writer (str (->> (get lines line-idx)
                             (split-at word-idx)
                             reverse
                             (apply concat)
                             (str/join " "))
                        "\n"))))

(defn -main
  [in out]
  (let [lines (read-lines in)
        shifts (circular-shifts lines)
        sorted-shifts (alphabetize lines shifts)]
    (with-open [w (writer out)]
      (output lines sorted-shifts w))))
