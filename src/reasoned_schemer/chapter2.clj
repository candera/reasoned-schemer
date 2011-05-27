;;; -*- mode: clojure; mode: clojure-test -*-
(ns reasoned-schemer.chapter2
  (:use reasoned-schemer.core
        ;; clojure.core.logic.prelude
        clojure.core.logic.minikanren
        clojure.test)
  (:refer-clojure :exclude [== inc reify]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; CHAPTER 2
;;
;; Teaching Old Toys New Tricks
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest p1
  (is (= :c
       (let [x (fn [a] a)
                y :c]
         (x y)))))

(deftest p2
  (is (= [['_.0 '_.1]]
       (run* [r]
             (exist [y x]
                    (== [x y] r))))))

(deftest p3
  (is (= [['_.0 '_.1]]
       (run* [r]
             (exist [v w]
                    (== (let [x v
                              y w]
                          [x y])
                        r))))))

(deftest p6
  (is (= [:a]
       (run* [r]
             (firsto [:a :c :o :r :n] r)))))

