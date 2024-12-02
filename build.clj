(ns build
  (:require [clojure.tools.build.api :as b]))

(def target "target")
(def class-dir (str target "/classes"))
(def lib 'codes.bauer/aoc2024)
(def version (format "1.0.%s" (b/git-count-revs nil)))
(def basis (b/create-basis {:project "deps.edn"}))
(def jar-file (format "%s/%s-%s.jar" target (name lib) version))
(def uberjar-file (format "%s/%s-%s-standalone.jar" target (name lib) version))

(defn clean [_]
  (b/delete {:path target})
  (println (format "Build folder \"%s\" removed" target)))

(defn jar [_]
  (clean nil)
  (b/write-pom {:class-dir target
                :lib       lib
                :version   version
                :basis     basis
                :src-dirs  ["src"]})
  (b/copy-dir {:src-dirs   ["src" "resources"]
               :target-dir target})
  (b/jar {:class-dir target
          :jar-file  jar-file})
  (println (format "Jar file created: \"%s\"" jar-file)))

(defn uberjar [_]
  (clean nil)
  (b/copy-dir {:src-dirs   ["resources"]
               :target-dir class-dir})
  (b/compile-clj {:basis     basis
                  :src-dirs  ["src"]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file uberjar-file
           :basis     basis})
  (println (format "Uber file created: \"%s\"" uberjar-file)))

(defn install [_]
  (b/write-pom {:class-dir target
                :lib       lib
                :version   version
                :basis     basis
                :src-dirs  ["src"]})
  (b/install {:basis basis
              :lib   lib
              :version version
              :jar-file uberjar-file
              :class-dir target})
  (println (format "Installed uberjar: \"%s\"" uberjar-file)))
