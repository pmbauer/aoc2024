(ns codes.bauer.aoc2024.07
  (:require [clojure.java.io :refer [resource reader]]
            [clojure.string :as str]
            [hyperfiddle.rcf :refer [tests]]))

(defn read-eqs [name]
  (with-open [r (reader (resource name))]
    (->> (line-seq r)
         (map #(map Long/parseLong (str/split % #":* ")))
         (map #(list* (first %) 0 (rest %)))
         (into []))))

(defn valid?-fn [ops]
  (fn valid? [expected [acc & tail]]
    (if (empty? tail)
      (= expected acc)
      (when (<= acc expected)
        (let [[head & tail'] tail
              valid-4-op? #(valid? expected (cons (% acc head) tail'))]
          (some valid-4-op? ops))))))

(defn solve [valid? resource-name]
  (transduce (comp
              (filter #(valid? (first %) (rest %)))
              (map first))
             +
             (read-eqs resource-name)))

(def part1-ops [+ *])
(def part2-ops [+ * #(Long/parseLong (str %1 %2))])

(tests
 (solve (valid?-fn part1-ops) "07_ex.in") := 3749
 (solve (valid?-fn part2-ops) "07_ex.in") := 11387)

(comment
  (time (solve (valid?-fn part1-ops) "07.in"))
  (time (solve (valid?-fn part2-ops) "07.in"))
  (comment))
