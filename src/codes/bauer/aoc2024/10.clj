(ns codes.bauer.aoc2024.10
  (:require [clojure.java.io :refer [resource reader]]
            [clojure.math.combinatorics :as combo]
            [hyperfiddle.rcf :refer [tests]]))

(def deltas #{[0 -1] [0 1] [1 0] [-1 0]})

(defn read-grid [name]
  (with-open [r (reader (resource name))]
    (into []
          (comp (map #(into [] (comp (map str) (map Long/parseLong)) %))
                (map vec))
          (line-seq r))))

(defn lookup [grid [x y]]
  (when (and (< -1 x (count grid)) (< -1 y (count grid)))
    (nth (nth grid y) x)))

(defn trailheads [grid]
  (into #{} (filter #(= 0 (lookup grid %))) (combo/selections (range (count grid)) 2)))

(defn trace [grid [head :as path]]
  (let [level' (inc (lookup grid head))]
    (if (= 10 level')
      [path]
      (into []
            (comp (map #(map + head %))
                  (filter #(when-let [level (lookup grid %)]
                             (= level' level)))
                  (map (partial conj path))
                  (mapcat (partial trace grid)))
            deltas))))

(defn part1 [grid]
  (->> (trailheads grid)
       (map #(->> (trace grid (list %))
                  (map first)
                  (distinct)
                  count))
       (reduce +)))

(defn part2 [grid]
  (->> (trailheads grid)
       (map #(trace grid (list %)))
       (map count)
       (reduce +)))

(tests
 (part1 (read-grid "10_ex.in")) := 36
 (part2 (read-grid "10_ex.in")) := 81)

(comment
  (def grid (read-grid "10.in"))
  (time (part1 grid))
  (time (part2 grid))
  (comment))
