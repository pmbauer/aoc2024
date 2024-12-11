(ns codes.bauer.aoc2024.11
  (:require [hyperfiddle.rcf :refer [tests]]))

(defn count-digits
  ([n count] (if (= 0 n) count (recur (quot n 10) (inc count))))
  ([n] (count-digits n 0)))

(def powers-of-10 (long-array [1 10 100 1000 10000 100000 1000000 10000000 100000000 1000000000]))

(defn split [n len]
  (let [div (nth powers-of-10 (quot len 2))]
    [(quot n div) (mod n div)]))

(defn split-even [n]
  (let [len (count-digits n)]
    (when (even? len)
      (split n len))))

(declare blink-stone)

;; returns stone count at n blinks
(defn blink-stone' [stone n]
  (if (= n 0)
    1
    (if (= 0 stone)
      (blink-stone 1 (dec n))
      (if-let [[a b] (split-even stone)]
        (+' (blink-stone a (dec n))
            (blink-stone b (dec n)))
        (blink-stone (* 2024 stone) (dec n))))))

(def blink-stone (memoize blink-stone'))

(defn blink [row n]
  (reduce +' (pmap #(blink-stone % n) row)))

(tests
 (blink [125 17] 25) := 55312)

(comment
  (do (require '[clojure.tools.trace :as t])
      (t/trace-ns *ns*))
  (t/untrace-ns *ns*)

  (def row [4189 413 82070 61 655813 7478611 0 8])

  ;; part 1
  (time (blink row 25))

  ;; part 2
  (time (blink row 75))

  ;; why not?
  (time (blink row 750))
  (comment))
