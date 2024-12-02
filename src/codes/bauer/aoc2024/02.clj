(ns codes.bauer.aoc2024.02
  "aoc 01"
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
  (vec (concat (subvec v 0 n)
               (subvec v (inc n)))))

(defn status-b [report]
  (let [status (status-a report)]
    (if (not= status ::unsafe)
      status
      (if-let [status (->> (range (count report))
                           (map #(status-a (remove-nth report %)))
                           (some #{::increasing ::decreasing}))]
        status
        ::unsafe))))

(def csv-integer-xform
  (comp (map str/trim)
        (map Integer/parseInt)))

(defn categorize-xform [status-fn]
  (comp (map #(into [] csv-integer-xform (str/split % #",")))
        (map status-fn)))

(comment
  (range (count [0 0]))

  (with-open [r (reader (resource "02a.csv"))]
    (->> (line-seq r)
         (into [] (comp (categorize-xform status-a)
                        (filter #{::increasing ::decreasing})))
         count))

  (with-open [r (reader (resource "02a.csv"))]
    (->> (line-seq r)
         (into [] (comp (categorize-xform status-b)
                        (filter #{::increasing ::decreasing})))
         count))

  (comment))
