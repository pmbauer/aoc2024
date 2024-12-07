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

(defn plot-path [[room loc]]
  (lazy-seq (if-let [next-loc (step room loc)]
              (cons loc (plot-path [room next-loc]))
              [loc])))

(defn safe-path
  ([room-and-loc] (safe-path #{} (plot-path room-and-loc)))
  ([traversed? path]
   (lazy-seq
    (when-let [s (seq path)]
      (let [next-step (first s)]
        (if (traversed? next-step)
          [::loop]
          (cons next-step (safe-path (conj traversed? next-step) (rest s)))))))))

(defn find-loops [[room loc]]
  (->> (for [y (range (count room))
             x (range (count (first room)))]
         (when (= \. (nth (nth room y) x))
           (let [room' (assoc-in room [y x] \#)]
             (when (= ::loop (last (safe-path [room' loc])))
               [x y]))))
       (filter identity)))

(defn part1 [room-name]
  (->> (read-room room-name)
       (safe-path)
       (map (partial take 2))
       (into #{})
       count))

(defn part2 [room-name]
  (->> (read-room room-name) (find-loops) count))

(tests
 (part1 "06_ex.in") := 41
 (part2 "06_ex.in") := 6)

(comment
  (part1 "06.in")
  (part2 "06.in")
  (comment))
