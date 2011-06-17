;;; -*- mode: clojure; mode: clojure-test -*-
(ns reasoned-schemer.chapter3
  (:use reasoned-schemer.core
        clojure.core.logic.prelude
        clojure.core.logic.minikanren
        clojure.test)
  (:refer-clojure :exclude [== inc reify list?]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; CHAPTER 3 
;;
;; Seeing Old Friends in New Ways
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;; We don't use clojure.core/list? because we don't particularly care
;;; in Clojure about whether somethign is exactly a list - we care
;;; whether it's a sequence

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
   ((emptyo l) s#)
   ((pairo l)
    (exist [d]
           (resto l d)
           (listo d)))))

(deftest p7
  (is (= '[_.0]
         (run* [x]
               (listo (list :a :b x :d))))))

;; Note that, unlike in Scheme, there's a difference between nil and
;; an empty list.
(deftest p10
  (is (= [[]]
         (run 1 [x]
               (listo (llist :a :b :c x))))))

(deftest p13
  (is (try
        (doall
         (run* [x]
               (listo (llist :a :b :c x))))
        false
        (catch StackOverflowError _ true))))

(deftest p14
  (is (= [[]
          ['_.0]
          ['_.0 '_.1]
          ['_.0 '_.1 '_.2]
          ['_.0 '_.1 '_.2 '_.3]]
         (run 5 [x]
              (listo (llist :a :b :c x))))))

(defn lol? [l]
  (cond
   (empty? l) true
   (list? (first l)) (lol? (rest l))
   :else false))

(deftest p16
  (is (lol? '((:a :b) (1 2))))
  (is (not (lol? '(1 2))))
  (is (lol? '())))

(defn lolo [l]
  (conde
   ((emptyo l) s#)
   ((exist [a]
           (firsto l a)
           (listo a))
    (exist [d]
           (resto l d)
           (lolo d)))))

(deftest p20
  (is (= ['()]
         (run 1 [l]
              (lolo l)))))

(deftest p21
  (is (= [true]
         (run* [q]
               (exist [x y]
                      (lolo '((:a :b) (x :c) (:d y)))
                      (== true q))))))

(deftest p22
  (is (= [true]
         (run 1 [q]
              (exist [x]
                     (lolo (llist '(:a :b) x))
                     (== true q))))))

(deftest p23
  (is (= ['()]
         (run 1 [x]
              (lolo (llist '(:a :b) '(:c :d) x))))))

;; TRS has this as
;; (()
;;  (())
;;  (() ())
;;  (() () ()) 
;;  (() () () ()) 
(deftest p24
  (is (= ['()
          '(())
          '((_.0))
          '(() ())
          '((_.0 _.1))]
         (run 5 [x]
              (lolo (llist '(:a :b) '(:c :d) x))))))

(defn twinso [s]
  (exist [x y]
         (conso x y s)
         (conso x '() y)))

(deftest p32
  (is (= [true]
         (run* [q]
               (twinso '(:tofu :tofu))
               (== true q)))))

(deftest p33
  (is (= [:tofu]
         (run* [z]
               ;; Note: do not write this as '(z tofu)! Quoting z
               ;; means that we're looking for the symbol z, not the
               ;; logic variable z. Found this out the hard way. :p
               (twinso (list z :tofu))))))

(defn twinso-36 [s]
  (exist [x]
         (== (list x x) s)))

(deftest p36
  (is (= [:tofu]
         (run* [q]
               (twinso-36 (list :tofu q))))))
