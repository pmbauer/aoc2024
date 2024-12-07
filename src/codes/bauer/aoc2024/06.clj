(ns codes.bauer.aoc2024.06
  (:require [clojure.java.io :refer [resource reader]]
            [hyperfiddle.rcf :refer [tests]]))

(def deltas {:N [0 -1] :S [0 1] :E [1 0] :W [-1 0]})
(def next-direction {:N :E, :E :S, :S :W, :W :N})

(defn read-room [room-name]
  (with-open [r (reader (resource room-name))]
    (let [room (->> r line-seq (map vec) (into []))]
      [room (first (for [y (range (count room))
                         x (range (count (first room)))
                         :when (= \^ (nth (nth room y) x))]
                     [x y :N]))])))

(defn lookup [room [x y _]]
  (when (and (< -1 x (count room)) (< -1 y (count room)))
    (nth (nth room y) x)))

(defn move1 [[x y dir]]
  (let [[dx dy] (deltas dir)]
    [(+ x dx) (+ y dy) dir]))

(defn step [room [x y dir :as loc]]
  (let [loc' (move1 loc)]
    (when-let [v (lookup room loc')]
      (if (= v \#)
        (recur room [x y (next-direction dir)])
        loc'))))

(defn plot-path
  ([[room loc]]
   (plot-path #{} (->> (iterate (partial step room) loc)
                       (take-while identity))))
  ([traversed? path]
   (lazy-seq
    (when-let [s (seq path)]
      (let [next-step (first s)]
        (if (traversed? next-step)
          [::loop]
          (cons next-step (plot-path (conj traversed? next-step) (rest s)))))))))

(defn find-loops [[room loc :as ral]]
  ;;               guard path             [x y dir] -> [x y]     unique
  (for [[x y] (->> (rest (plot-path ral)) (map (partial take 2)) (into #{}))
        :let [room' (assoc-in room [y x] \#)]
        :when (= ::loop (last (plot-path [room' loc])))]
    [x y]))

(defn part1 [room-name]
  (->> (read-room room-name) (plot-path) (map (partial take 2)) (into #{}) count))

(defn part2 [room-name]
  (->> (read-room room-name) (find-loops) count))

(tests
 (part1 "06_ex.in") := 41
 (part2 "06_ex.in") := 6)

(comment
  (part1 "06.in")
  (time (part2 "06.in"))
  (comment))
