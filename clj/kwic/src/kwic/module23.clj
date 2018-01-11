(ns kwic.module23
  (:require [clojure.string :as str]
            [clojure.java.io :refer [reader writer]]))

;; Transducers

(defn read-lines
  [input]
  (-> input
      slurp
      str/split-lines))

(def parse-lines
  (map #(str/split % #" ")))

(def circular-shifts
  (mapcat (fn [line]
            (map #(apply concat
                         (reverse (split-at % line)))
                 (range (count line))))))

(defn alphabetize
  ([results]
   (mapcat val results))
  ([sm line]
   (update-in sm [(str/lower-case (first line))] conj line)))

(defn output
  [lines writer]
  (doseq [line lines]
    (.write writer (str (str/join " " line) "\n"))))

(defn -main
  [in out]
  (let [lines (read-lines in)
        process (comp parse-lines circular-shifts)]
    (with-open [w (writer out)]
      (output (transduce process alphabetize (sorted-map) lines) w))))
