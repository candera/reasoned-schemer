(ns reasoned-schemer.core
  (:use clojure.core.logic.minikanren
        clojure.core.logic.prelude)
  (:refer-clojure :exclude [== inc reify]))

(defn set=
  "Returns true if a and b have the same elements, regardless of order"
  [a b]
  (= (set a) (set b)))

(defn pair?
  "Returns true if x is a pair-like thing. The slightly awkward
  definition arises out of the mismatch between Scheme and Clojure."
  [x]
  (or (lcons? x) (and (coll? x) (seq x))))

(defn pairo
  "Succeeds if p is a pair-like thing."
  [p]
  (exist [a d]
    (== (lcons a d) p)))
