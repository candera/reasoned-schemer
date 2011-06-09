;;; -*- mode: clojure; mode: clojure-test -*-
(ns reasoned-schemer.chapter3
  (:use reasoned-schemer.core
        clojure.core.logic.prelude
        clojure.core.logic.minikanren
        clojure.test)
  (:refer-clojure :exclude [== inc reify]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; CHAPTER 3 
;;
;; Seeing Old Friends in New Ways
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;; We don't use list? because we don't particularly care in Clojure
;;; about whether somethign is exactly a list - we care whether
;;; it's a sequence

(def list? seq?)

(deftest p1
  (is (= true
         (list? '([:a] [:a :b] :c)))))

(deftest p2
  (is (= true
         (list? '()))))

(deftest p3
  (is (= false
         (list? :s))))

(deftest p4
  (is (= false
         (list? (llist :d :a :t :e :s)))))

(defn listo [l]
  (conde
   ((nilo l) s#)
   ((pairo l)
    (exist [d]
           (resto l d)
           (listo d)))))

(deftest p7
  (is (= '[_.0]
         (run* [x]
               (listo (list :a :b x :d))))))

;;; This one is really pretty weird, since llist doesn't produce a
;;; Clojure list. This whole section of the book suffers from the
;;; impedance mismatch between what's available in Scheme and
;;; Clojure's more advanced data structures.
(deftest p10
  (is (= []
         (run 1 [x]
               (listo (llist :a :b :c x))))))

(run 5 [x]
     (listo x))