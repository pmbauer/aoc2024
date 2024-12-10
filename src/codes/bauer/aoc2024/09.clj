(ns codes.bauer.aoc2024.09
  (:require [clojure.java.io :refer [resource]]
            [clojure.string :as str]
            [hyperfiddle.rcf :refer [tests]]))

(defn read-disk [s]
  (let [in (into [] (map #(Long/parseLong (str %))) (str/trim-newline s))
        files (->> (take-nth 2 in) (map-indexed #(repeat %2 %1)))
        free (-> (take-nth 2 (rest in)) (concat [0]) (->> (map #(repeat % \.))))]
    (into [] (apply concat (interleave files free)))))

(defn fragment [v]
  (let [v' (transient v), i (volatile! 0), j (volatile! (dec (count v)))]
    (while (< @i @j)
      (while (= \. (nth v' @j))
        (vswap! j dec))
      (while (number? (nth v' @i))
        (vswap! i inc))
      (when (< @i @j)
        (let [val (nth v' @i)]
          (-> (assoc! v' @i (nth v' @j))
              (assoc! @j val)))))
    (persistent! v')))

(defn index-disk [coll]
  (-> (reduce
       (fn [{:keys [offset] :as idx} [v :as f]]
         (let [len (count f)]
           (-> (if (= \. v)
                 (update idx \. conj [offset len])
                 (assoc idx v [offset len]))
               (update :offset + len))))
       {:offset 0}
       (partition-by identity coll))
      (update \. #(into [] (reverse %)))
      (dissoc :offset)))

(defn first-free [free-list [offset len]]
  (loop [i 0]
    (let  [[free-offset free-len :as free] (nth free-list i)]
      (when (< free-offset offset)
        (if (<= len free-len)
          [i free]
          (recur (inc i)))))))

(defn defrag [coll]
  (let [idx (index-disk coll)
        {free \. :as idx'}
        (->> (dissoc idx \.) (sort-by first) (reverse)
             (reduce
              (fn [{free \. :as idx} [file [offset len :as pos]]]
                (if-let [[free-index [free-offset free-len]] (first-free free pos)]
                  (let [free-path [\. free-index]]
                    (-> (assoc idx file [free-offset len])
                        (assoc-in free-path [(+ free-offset len) (- free-len len)])
                        (update \. conj [offset len])))
                  idx))
              idx))]
    (into []
          (mapcat (fn [[x [_ n]]] (repeat n x)))
          (sort-by (comp first second)
                   (concat (dissoc idx' \.)
                           (map #(vector \. %) free))))))

(defn checksum [v]
  (transduce (comp (map-indexed #(when (number? %2) (* %1 %2)))
                   (filter (complement nil?)))
             + v))

(tests
 (defn atodisk [s]
   (into [] (map #(if (<= (int \0) (int %) (int \9)) (long (- (int %) (int \0))) %) s)))
 (defn disktoa [coll] (reduce str coll))

 (def example (read-disk "2333133121414131402"))

 (-> example disktoa) := "00...111...2...333.44.5555.6666.777.888899"
 (-> example fragment disktoa) := "0099811188827773336446555566.............."
 (-> example fragment checksum) := 1928
 (-> example defrag disktoa) := "00992111777.44.333....5555.6666.....8888.."
 (-> example defrag checksum) := 2858)

(comment
  (def disk (read-disk (slurp (resource "09.in"))))
  ;; part 1
  (-> disk fragment checksum time)
  ;; part 2
  (-> disk defrag checksum time)
  (comment))
