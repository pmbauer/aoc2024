(ns codes.bauer.aoc2024.03
  "aoc 03"
  (:require [clojure.java.io :refer [resource]]
            [hyperfiddle.rcf :refer [tests]]
            [instaparse.core :as insta]))

(def grammar1
  (insta/parser "
    <input> := (<any> | mul)*
    mul     := <'mul('> nr <','> nr <')'>
    <nr>    := #'\\d{1,3}'
    any     := #'[\\s\\S]'"))

(defn parse1 [input]
  (->> (insta/parse grammar1 input)
       (map #(conj (map Long/parseLong (rest %)) '*))
       (apply list '+)))

(def grammar2
  (insta/parser "
    <input> := (<any> | mul | on | off)*
    mul     := <'mul('> nr <','> nr <')'>
    on      := <'do()'>
    off     := <'don\\'t()'>
    <nr>    := #'\\d{1,3}'
    any     := #'[\\s\\S]'"))

(defn parse2 [input]
  (->> (insta/parse grammar2 input)
       (map (fn [[op & args]]
              (condp = op
                :mul (list* '* (map Long/parseLong args))
                (symbol op))))
       (cons 'on)))

(def on +)
(def off (constantly 0))

(defn eval2 [exprs]
  (transduce (let [s (atom true)]
               (comp
                ;; group into ([on|off] (* x y)...) function calls
                (partition-by #(if (#{'on 'off} %) (swap! s not) @s))
                (map list*)
                (map eval)))
             +
             exprs))

(tests
 (parse1 "$ xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+\nmul(32,64]then(mul(11,8)mul(8,5))")
 := '(+ (* 2 4) (* 5 5) (* 11 8) (* 8 5))

 (parse2 "$ xmul(2,4)&mul[3,7]!^don't()_mul(5,5)\n+mul(32,64](mul(11,8)undo()?do() mul(8,5))")
 := '(on (* 2 4) off (* 5 5) (* 11 8) on on (* 8 5))

 (eval2 '(on (* 2 4) off (* 5 5) (* 11 8) on on (* 8 5)))
 := 48)

(comment
  ;; part 1
  (-> (resource "03.in") slurp parse1 eval)

  ;; part 2
  (-> (resource "03.in") slurp parse2 eval2)

  (comment))
