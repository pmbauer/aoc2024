(ns codes.bauer.aoc2024.04
  (:require [clojure.java.io :refer [resource reader]]
            [clojure.string :as str]
            [hyperfiddle.rcf :refer [tests]]))

(defn read-grid [resource-name]
  (with-open [r (reader (resource resource-name))]
    (->> (line-seq r) (map vec) (into []))))

(def all-directions #{[0 1] [0 -1] [1 0] [1 1] [1 -1] [-1 0] [-1 1] [-1 -1]})

(defn lookup [g [^long x ^long y]]
  (if (and (< -1 x (count g)) (< -1 y (count g)))
    (nth (nth g y) x)
    (char 0)))

(defn path [coord [dx dy] len]
  (take len (iterate (fn [[x y]] [(+ x dx) (+ y dy)]) coord)))

(defn path-word [g p]
  (apply str (map (partial lookup g) p)))

(defn path-words [g len directions]
  (for [x (range (count g))
        y (range (count g))
        delta directions]
    (path-word g (path [x y] delta len))))

(defn count-instances [g word]
  (->> (path-words g (count word) all-directions)
       (filter (partial = word))
       (count)))

(defn sub-grid [g [x y] len]
  (vec (for [y' (range len)]
         (vec (for [x' (range len)]
                (lookup g [(+ x x') (+ y y')]))))))

(defn X? [g word]
  (let [size (count word)
        drow (str/reverse word)
        w1 (path-word g (path [0 0] [1 1] size))
        w2 (path-word g (path [0 (dec size)] [1 -1] size))]
    (and (or (= w1 word) (= w1 drow))
         (or (= w2 word) (= w2 drow)))))

(defn sub-grids [g len]
  (let [bound (inc (- (count g) len))]
    (for [x (range bound)
          y (range bound)]
      (sub-grid g [x y] len))))

(defn count-Xs [g word]
  (->> (sub-grids g (count word))
       (filter #(X? % word))
       count))

(tests
 (let [g [[\0 \1 \2] [\3 \4 \5] [\6 \7 \8]]]
   (map (partial lookup g) [[0 0] [2 2] [-1 0] [3 3]]))
 := [\0 \8 (char 0) (char 0)]

 (-> (read-grid "04_example.in")
     (count-instances "XMAS"))
 := 18

 (X? [[\S \M \S]
      [\X \A \M]
      [\M \M \M]] "MAS")
 := true

 (sub-grids [[\S \M \S]
             [\X \A \M]
             [\M \M \M]] 2)
 := [[[\S \M]
      [\X \A]]
     [[\X \A]
      [\M \M]]
     [[\M \S]
      [\A \M]]
     [[\A \M]
      [\M \M]]]

 (-> (read-grid "04_example.in")
     (count-Xs "MAS"))
 := 9)

(comment
;; part 1
  (-> (read-grid "04.in")
      (count-instances "XMAS"))

  ;; part 2
  (-> (read-grid "04.in")
      (count-Xs "MAS"))
  (comment))
