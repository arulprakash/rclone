(ns edn.datagen
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))
(defn has-length?
  ([s l]
   (= (count s) l))
  ([s l1 l2]
   (contains? (set (range l1 (inc l2))) (count s))))

(s/def ::l30 (has-length?))
