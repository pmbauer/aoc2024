(ns codes.bauer.aoc2024.05
  (:require [clojure.java.io :refer [resource reader]]
            [clojure.string :as str]
            [hyperfiddle.rcf :refer [tests]]))

(defn read-plans [rules-name updates-name]
  (letfn [(conj-set [coll x] (if coll (conj coll x) #{x}))]
    (with-open [rules-reader (reader (resource rules-name))
                updates-reader (reader (resource updates-name))]
      {:rules (->> (line-seq rules-reader)
                   (map #(map Long/parseLong (str/split % #"\|")))
                   (reduce (fn [rules [before after]]
                             (update-in rules [before] conj-set after))
                           {}))
       :updates (->> (line-seq updates-reader)
                     (map #(map Long/parseLong (str/split % #",")))
                     (into []))})))

(defn valid?-fn [rules]
  (fn [update-seq]
    (every? (partial get-in rules) (partition 2 1 update-seq))))

(defn page-comparator [rules]
  (comparator (comp (partial get-in rules) vector)))

(defn middle-sum [xs]
  (->> (map #(nth % (quot (count %) 2)) xs) (reduce +)))

(defn part1 [rules-name updates-name]
  (-> (read-plans rules-name updates-name)
      (as-> {:keys [rules updates]}
            (filter (valid?-fn rules) updates))
      middle-sum))

(defn part2 [rules-name updates-name]
  (-> (read-plans rules-name updates-name)
      (as-> {:keys [rules updates]}
            (->> (filter (complement (valid?-fn rules)) updates)
                 (map #(sort (page-comparator rules) %))
                 middle-sum))))

(tests
 (part1 "05_rules_ex.in" "05_updates_ex.in") := 143
 (part2 "05_rules_ex.in" "05_updates_ex.in") := 123)

(comment
  (part1 "05_rules.in" "05_updates.in")
  (part2 "05_rules.in" "05_updates.in")
  (comment))
