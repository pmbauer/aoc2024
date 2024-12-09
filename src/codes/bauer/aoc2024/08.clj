(ns codes.bauer.aoc2024.08
  (:require [clojure.java.io :refer [resource reader]]
            [clojure.math.combinatorics :as combo]
            [hyperfiddle.rcf :refer [tests]]))

(defn read-grid [name]
  (with-open [r (reader (resource name))]
    (into [] (line-seq r))))

(defn lookup [grid [x y]]
  (when (and (< -1 x (count (first grid))) (< -1 y (count grid)))
    (nth (nth grid y) x)))

(defn group-towers [grid]
  (->> (for [y (range (count grid))
             x (range (count (first grid)))
             :let [v (lookup grid [x y])]
             :when (not= \. v)]
         [v [x y]])
       (group-by first)
       (map (fn [[_ group]] (map second group)))))

(defn part1 [name]
  (let [grid (read-grid name)
        antinodes (fn [a b]
                    [(map + a (map - a b))
                     (map + b (map - b a))])]
    (into #{}
          (comp (mapcat #(combo/combinations % 2))
                (mapcat #(apply antinodes %))
                (filter #(lookup grid %)))
          (group-towers grid))))

(defn antinodes2 [on-grid? a b]
  (concat
   (take-while on-grid? (iterate #(map + (map - a b) %) a))
   (take-while on-grid? (iterate #(map + (map - b a) %) b))))

(defn part2 [name]
  (let [grid (read-grid name)
        on-grid? #(lookup grid %)]
    (into #{}
          (comp (mapcat #(combo/combinations % 2))
                (mapcat #(apply antinodes2 on-grid? %)))
          (group-towers grid))))

(tests
 (count (part1 "08_ex.in")) := 14
 (count (part2 "08_ex.in")) := 34)

(comment
  (time (count (part1 "08.in")))
  (time (count (part2 "08.in")))
  (comment))
