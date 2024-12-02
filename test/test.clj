(ns test
  (:require
   [clojure.test]
   [hyperfiddle.rcf :as rcf]))

(def default-under-test
  '(codes.bauer.aoc2024.01))

(defn run [{:keys [namespaces]
            :or {namespaces default-under-test}}]
  (alter-var-root (var rcf/*generate-tests*) (constantly true))
  (apply require namespaces)
  (let [{:keys [fail error]} (apply clojure.test/run-tests namespaces)]
    (when (> (+ fail error) 0)
      (throw (ex-info "Test failures or errors occurred." {})))))
