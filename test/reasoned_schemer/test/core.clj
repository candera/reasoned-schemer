(ns reasoned-schemer.test.core
  (:use reasoned-schemer.core
        ;; clojure.core.logic.prelude
        clojure.core.logic.minikanren
        clojure.test)
  (:refer-clojure :exclude [== inc reify]))

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