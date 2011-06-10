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
  (is (= '[_.0]                         ; Fails. Returns no results
         (run* [x]
               (listo (list :a :b x :d))))))

;; Note that, unlike in Scheme, there's a difference between nil and
;; an empty list.
(deftest p10
  (is (= [nil]
         (run 1 [x]
               (listo (llist :a :b :c x))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; I'm seeing some things I don't understand. Here's what I get:

(run 1 [x]
     (listo (llist :a x :b)))           ; => ()

(run 1 [x]
     (listo (list :a x :b)))            ; => ()

(run 2 [x]
     (listo (llist :a :b x)))           ; => [nil (._0)]

(run 2 [x]
     (listo (list :a :b x)))            ; => ()


