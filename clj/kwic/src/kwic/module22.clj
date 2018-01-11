(ns kwic.module22
  (:require [clojure.string :as str]
            [clojure.java.io :refer [reader writer]]))

(defn read-lines
  [input]
  (->> input
       slurp
       str/split-lines
       (remove str/blank?)
       (map #(str/split % #" "))))

(defn circular-shifts
  [lines]
  (mapcat (fn [line]
            (map #(apply concat
                         (reverse (split-at % line)))
                 (range (count line))))
          lines))

(defn alphabetize
  [lines]
  (sort-by #(str/lower-case (first %))
           lines))

(defn output
  [lines writer]
  (doseq [line lines]
    (.write writer (str (str/join " " line) "\n"))))

(defn -main
  [in out]
  (with-open [w (writer out)]
    (-> in
        read-lines
        circular-shifts
        alphabetize
        (output w))))
