(ns kwic.module21
  (:require [clojure.string :as str]
            [clojure.java.io :refer [reader writer]]))

;; The "JAVA Way" (Just for illustration purpose! Not good clojure code.)

(defprotocol LineStorage
  (line-count [_])
  (line-at [_ line-idx])
  (word-count [_ line-idx])
  (word-at [_ line-idx word-idx]))

(defn read-lines
  [input]
  (let [lines (->> input
                   slurp
                   str/split-lines
                   (remove str/blank?)
                   (map #(into [] (str/split % #" ")))
                   (into []))]
    (reify LineStorage
      (line-count [_]
        (count lines))
      (line-at [_ line-idx]
        (get lines line-idx))
      (word-count [this line-idx]
        (count (line-at this line-idx)))
      (word-at [this line-idx word-idx]
        (get (line-at this line-idx) word-idx)))))

(defn circular-shifts
  [line-storage]
  (let [shifts (->> (map (fn [line-idx]
                           (map (fn [word-idx]
                                  [line-idx word-idx])
                                (range (word-count line-storage line-idx))))
                         (range (line-count line-storage)))
                    (apply concat)
                    (into []))]
    (reify LineStorage
      (line-count [_]
        (count shifts))
      (line-at [_ line-idx]
        (let [[line-idx word-idx] (get shifts line-idx)]
          (->> (line-at line-storage line-idx)
               (split-at word-idx)
               reverse
               (apply concat))))
      (word-count [this line-idx]
        (count (line-at this line-idx)))
      (word-at [this line-idx word-idx]
        (get (line-at this line-idx) word-idx)))))

(defn alphabetize
  [line-storage]
  (let [sorted-lines (->> (map #(line-at line-storage %)
                               (range (line-count line-storage)))
                          (sort-by #(str/lower-case (first %)))
                          (into []))]
    (reify LineStorage
      (line-count [_]
        (line-count line-storage))
      (line-at [_ line-idx]
        (get sorted-lines line-idx))
      (word-count [this line-idx]
        (count (line-at this line-idx)))
      (word-at [this line-idx word-idx]
        (get (line-at this line-idx) word-idx)))))

(defn output
  [line-storage writer]
  (doseq [line-idx (range (line-count line-storage))]
    (.write writer (str (str/join " " (line-at line-storage line-idx))
                        "\n"))))

(defn -main
  [in out]
  (with-open [w (writer out)]
    (-> in
        read-lines
        circular-shifts
        alphabetize
        (output w))))
