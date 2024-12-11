(ns codes.bauer.aoc2024.11
  (:require [clojure.string :as str]
            [hyperfiddle.rcf :refer [tests]]))

(def read-row #(map Long/parseLong (str/split % #"\s")))

(defn count-digits
  ([n count] (if (= 0 n) count (recur (quot n 10) (inc count))))
  ([n] (count-digits n 0)))

(defn split [n]
  (let [ns (str n), len (count ns), mid (quot len 2)]
    [(Long/parseLong (subs ns 0 mid))
     (Long/parseLong (subs ns mid len))]))

(defn split-even [n]
  (when (even? (count-digits n)) (split n)))

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
  (reduce +' (map #(blink-stone % n) row)))

(tests
 (-> (read-row "125 17") (blink 25)) := 55312)

(comment
  (def row (read-row "4189 413 82070 61 655813 7478611 0 8"))

  ;; part 1
  (time (blink row 25))

  ;; part 2
  (time (blink row 75))

  ;; why not?
  (time (blink row 750))
  (comment))
