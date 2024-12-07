(ns codes.bauer.aoc2024.06
  (:require [clojure.java.io :refer [resource reader]]
            [clojure.string :as str]
            [hyperfiddle.rcf :refer [tests]]))

(def deltas {:N [0 -1] :S [0 1] :E [1 0] :W [-1 0]})
(def next-direction {:N :E, :E :S, :S :W, :W :N})

(defn read-room [room-name]
  (with-open [r (reader (resource room-name))]
    (let [room (->> r line-seq (map vec) (into []))]
      [room (first (for [y (range (count room))
                         x (range (count (first room)))
                         :when (= \^ (get-in room [y x]))]
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
        (step room [x y (next-direction dir)])
        loc'))))

(defn plot-path [room loc]
  (lazy-seq (if-let [next-loc (step room loc)]
              (cons loc (plot-path room next-loc))
              [loc])))

(defn part1 [room-name]
  (->> (read-room room-name)
       (apply plot-path)
       (map (partial take 2))
       (into #{})
       count))

(tests
 (part1 "06_ex.in") := 41)

(comment
  (defn render [room-name]
    (let [[room loc] (read-room room-name)
          path (plot-path room loc)]
      (->> (reduce (fn [r [x y]] (assoc-in r [y x] \X)) room path)
           (map #(apply str %)))))

  (render "06_ex.in")

  (part1 "06.in")
  (comment))
