;;; -*- mode: clojure; mode: clojure-test -*-
(ns reasoned-schemer.chapter1
  (:use reasoned-schemer.core
        ;; clojure.core.logic.prelude
        clojure.core.logic.minikanren
        clojure.test)
  (:refer-clojure :exclude [== inc reify]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; CHAPTER 1
;;
;; Playthings
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest p40
  (is (= [true]
         (run* [q]
               (exist [x]
                      (== true x)
                      (== x q))))))

(deftest p47
  (is (= [:olive :oil]
           (run* [x]
                 (conde
                  ((== :olive x))
                  ((== :oil x)))))))

(deftest p49
  (is (= [:olive]
       (run 1 [x]
             (conde
              ((== :olive x))
              ((== :oil x)))))))

(deftest p50
  (is (= [:olive '_.0 :oil]
           (run* [x]
            (conde
             ((== :virgin x) u#)
             ((== :olive x) s#)
             (s# s#)
             ((== :oil x) s#))))))

(deftest p52
  (is (= [:extra :olive]
           (run 2 [x]
                (conde
                 ((== :extra x) s#)
                 ((== :virgin x) u#)
                 ((== :olive x) s#)
                 ((== :oil x) s#))))))

(deftest p53
  (is (= [[:split :pea]]
       (run* [r]
             (exist [x y]
                    (== :split x)
                    (== :pea y)
                    (== (cons x (cons y '())) r))))))

(deftest p54
  (is (= [[:split :pea] [:navy :bean]]
       (run* [r]
             (exist [x y]
                    (conde
                     ((== :split x) (== :pea y))
                     ((== :navy x) (== :bean y)))
                    (== [x y] r))))))

(deftest p55
  (is (= [[:split :pea :soup] [:navy :bean :soup]]
       (run* [r]
             (exist [x y]
                    (conde
                     ((== :split x) (== :pea y))
                     ((== :navy x) (== :bean y)))
                    (== [x y :soup] r))))))

(defn teacupo [x]
  (conde
   ((== :tea x) s#)
   ((== :cup x) s#)))

(deftest p56
  (is (= [:tea :cup]
         (run* [x]
               (teacupo x)))))

(deftest p57
  (is (set= [[:tea true] [:cup true] [false true]]
       (run* [r]
             (exist [x y]
                     (conde
                      ((teacupo x) (== true y) s#)
                      ((== false x) (== true y)))
                     (== [x y] r))))))

(deftest p58
  (is (set= [['_.0 '_.1] ['_.0 '_.1]]
       (run* [r]
             (exist [x y z]
                    (conde
                     ((== y x)
                      (exist [x] (== z x)))
                     ((exist [x] (== y x))
                      (== x z)))
                    (== [y z] r))))))

(deftest p59
  (is (set= [[false '_.0] ['_.0 false]]
       (run* [r]
            (exist [x y z]
                   (conde
                    ((== y x)
                     (exist [x] (== z x)))
                    ((exist [x] (== y x))
                     (== z x)))
                   (== false x)
                   (== [y z] r))))))

(deftest p60
  (is (set= [false]
       (run* [q]
             (let [a (== true q)
                   b (== false q)]
               b)))))

;; (run* [q]
;;       (conde
;;        (u# (== q :a))
;;        (s# (== q :b))))
;; => [:b]

(deftest p61
  (is (set= [false]
       (run* [q]
             (let [a (== true q)
                   b (exist [x]
                            (== x q)
                            (== false x))
                   c (conde
                      ((== true q))
                      (s# (== false q)))]
               b)))))

