;;; -*- mode: clojure; mode: clojure-test -*-
(ns reasoned-schemer.chapter3
  (:use reasoned-schemer.core
        [clojure.core.logic :exclude [is]]
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
    (fresh [d]
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
   ((fresh [a]
           (firsto l a)
           (listo a))
    (fresh [d]
           (resto l d)
           (lolo d)))))

(deftest p20
  (is (= ['()]
         (run 1 [l]
              (lolo l)))))

(deftest p21
  (is (= [true]
         (run* [q]
               (fresh [x y]
                      (lolo '((:a :b) (x :c) (:d y)))
                      (== true q))))))

(deftest p22
  (is (= [true]
         (run 1 [q]
              (fresh [x]
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
;;
;; The reason for the difference is that conde in the book is a
;; depth-first search where conde in core.logic is an interleaving
;; one.
(deftest p24
  (is (= ['()
          '(())
          '((_.0))
          '(() ())
          '((_.0 _.1))]
         (run 5 [x]
              (lolo (llist '(:a :b) '(:c :d) x))))))

(defn twinso [s]
  (fresh [x y]
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
  (fresh [x]
         (== (list x x) s)))

(deftest p36
  (is (= [:tofu]
         (run* [q]
               (twinso-36 (list :tofu q))))))

(defn loto [l]
  (conde
   ((emptyo l) s#)
   ((fresh [a]
           (firsto l a)
           (twinso a))
    (fresh [d]
           (resto l d)
           (loto d)))))

(deftest p38
  (is (= ['()]
         (run 1 [z]
              (loto (llist '(:g :g) z))))))

(deftest p42
  (is (= ['()
          '((_.0 _.0))
          '((_.0 _.0) (_.1 _.1))
          '((_.0 _.0) (_.1 _.1) (_.2 _.2))
          '((_.0 _.0) (_.1 _.1) (_.2 _.2) (_.3 _.3))]
         (run 5 [z]
              (loto (llist '(:g :g) z))))))

(deftest p45
  (is (= ['(:e (_.0 _.0) ())
          '(:e (_.0 _.0) ((_.1 _.1)))
          '(:e (_.0 _.0) ((_.1 _.1) (_.2 _.2)))
          '(:e (_.0 _.0) ((_.1 _.1) (_.2 _.2) (_.3 _.3)))
          '(:e (_.0 _.0) ((_.1 _.1) (_.2 _.2) (_.3 _.3) (_.4 _.4)))]
         (run 5 [r]
              (fresh [w x y z]
                     (loto (llist (list :g :g) (list :e w) (list x y) z))
                     (== r (list w (list x y) z)))))))
(deftest p47
  (is (= ['((:g :g) (:e :e) (_.0 _.0))
          '((:g :g) (:e :e) (_.0 _.0) (_.1 _.1))
          '((:g :g) (:e :e) (_.0 _.0) (_.1 _.1) (_.2 _.2))]
       (run 3 [out]
            (fresh [w x y z]
                   (== (llist (list :g :g) (list :e w) (list x y) z) out)
                   (loto out))))))

(defn listofo [predo l]
  (conde
   ((emptyo l) s#)
   ((fresh [a]
           (firsto l a)
           (predo a))
    (fresh [d]
           (resto l d)
           (listofo predo d)))))

(deftest p49
  (is (= ['((:g :g) (:e :e) (_.0 _.0))
          '((:g :g) (:e :e) (_.0 _.0) (_.1 _.1))
          '((:g :g) (:e :e) (_.0 _.0) (_.1 _.1) (_.2 _.2))]
         (run 3 [out]
              (fresh [w x y z]
                     (== out (llist (list :g :g) (list :e w) (list x y) z))
                     (listofo twinso out))))))
