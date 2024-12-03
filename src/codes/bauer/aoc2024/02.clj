(ns codes.bauer.aoc2024.02
  "aoc 02"
  (:require [clojure.string :as str]
            [clojure.java.io :refer [resource reader]]))

(defn safe? [report direction]
  (not= ::unsafe
        (reduce (fn [x0 x1]
                  (let [delta (* direction (- x1 x0))]
                    (if (<= 1 delta 3)
                      x1
                      (reduced ::unsafe))))
                (first report)
                (rest report))))

(defn status-a [report]
  (if (safe? report 1)
    ::increasing
    (if (safe? report -1)
      ::decreasing
      ::unsafe)))

(defn remove-nth [v n]
  (vec (concat (subvec v 0 n) (subvec v (inc n)))))

(defn status-b [report]
  (or (->> (range (count report))
           (map #(remove-nth report %))
           (cons report)
           (map status-a)
           (some #{::increasing ::decreasing}))
      ::unsafe))

(def csv-integer-xform
  (comp (map str/trim)
        (map Integer/parseInt)))

(defn count-safe [rname status-fn]
  (with-open [r (reader (resource rname))]
    (->> (line-seq r)
         (into [] (comp (map #(into [] csv-integer-xform (str/split % #",")))
                        (map status-fn)
                        (filter #{::increasing ::decreasing})))
         count)))

(comment
  ;; part 1
  (count-safe "02a.csv" status-a)

  ;; part 2
  (count-safe "02a.csv" status-b)

  (comment))
