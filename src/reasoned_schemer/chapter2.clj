;;; -*- mode: clojure; mode: clojure-test -*-
(ns reasoned-schemer.chapter2
  (:use reasoned-schemer.core
        clojure.core.logic.prelude
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

(deftest p7
  (is (= [true]
       (run* [q]
             (firsto [:a :c :o :r :n] :a)
             (== true q)))))

(deftest p8
  (is (= [:pear]
       (run* [r]
             (exist [x y]
                    (firsto [r y] x)
                    (== :pear x))))))

(comment
  (defn firsto [p a]
    (exist [d]
           (conso a d p))))

(deftest p11
  (is (= [[:grape :a]]
       (run* [r]
             (exist [x y]
                    (firsto [:grape :raisin :pear] x)
                    (firsto [[:a] [:b] [:c]] y)
                    (== (lcons x y) r))))))

(deftest p15
  (is (= [:c]
       (run* [r]
             (exist [v]
                    (resto [:a :c :o :r :n] v)
                    (firsto v r))))))

(comment
  (defn resto [p d]
    (exist [a]
           (== (lcons a d) p))))

(deftest p18
  (is (= [[[:raisin :pear] :a]]
       (run* [r]
             (exist [x y]
                    (resto [:grape :raisin :pear] x)
                    (firsto [[:a] [:b] [:c]] y)
                    (== (lcons x y) r))))))

(deftest p19
  (is (= [true]
       (run* [q]
             (resto [:a :c :o :r :n] [:c :o :r :n])
             (== true q)))))

(deftest p20
  (is (= [:o]
       (run* [x]
             (resto [:c :o :r :n] [x :r :n])))))

(deftest p21
  (is (= [[:a :c :o :r :n]]
       (run* [l]
             (exist [x]
                    (resto l [:c :o :r :n])
                    (firsto l x)
                    (== :a x))))))

(deftest p22
  (is (= [[[:a :b :c] :d :e]]
       (run* [l]
             (conso [:a :b :c] [:d :e] l)))))

(deftest p23
  (is (= [:d]
       (run* [x]
             (conso x [:a :b :c] [:d :a :b :c])))))

(deftest p24
  (is (= [[:e :a :d :c]]
       (run* [r]
             (exist [x y z]
                    (== [:e :a :d x] r)
                    (conso y [:a z :c] r))))))

(deftest p25
  (is (= [:d]
       (run* [x]
             (conso x [:a x :c] [:d :a x :c])))))

(deftest p26
  (is (= [[:d :a :d :c]]
       (run* [l]
             (exist [x]
                    (== [:d :a x :c] l)
                    (conso x [:a x :c] l))))))

(deftest p27
  (is (= [[:d :a :d :c]]
       (run* [l]
            (exist [x]
                   (conso x [:a x :c] l)
                   (== [:d :a x :c] l))))))

(defn p28-conso [a b o]
  (== (lcons a b) o))

(deftest p28
  (is (= [[:a :b]]
         (run* [o]
               (p28-conso :a [:b] o)))))

(deftest p29
  (is (= [[:b :e :a :n :s]]
         (run* [l]
               (exist [d x y w s]
                      (conso w [:a :n :s] s)
                      (resto l s)
                      (firsto l x)
                      (== :b x)
                      (resto l d)
                      (firsto d y)
                      (== :e y))))))

(deftest p32
  (is (= []
         (run* [q]
               (nilo [:grape :raisin :pear])
               (== true q)))))

(deftest p33
  (is (= [true]
         (run* [q]
               (emptyo [])
               (== true q)))))

(deftest p34
  (is (= [[]]
         (run* [x]
               (emptyo x)))))

(defn p35-nilo [x]
  (== [] x))

(deftest p35
  (is (= [[]]
         (run* [x]
               (p35-nilo x)))))

(deftest p38
  (is (= []
         (run* [q]
               (== :pear :plum)         ; Couldn't find an eqo
               (== true q)))))

(deftest p39
  (is (= [true]
         (run* [q]
               (== :plum :plum)
               (== true q)))))

;;; p40
(defn my-eqo [a b]
  (== a b))

(defn pair? [x]
  (satisfies? IPair x))

(deftest p41
  (is (pair? (pair :split :pea))))

(deftest p42
  (is (= [true]
         (run* [x]
               (exist [y]
                      (== x (pair? (pair :split y))))))))

(deftest p43
  (is (pair? (pair [:split] :pea))))

(deftest p44
  (is (not (pair? []))))

(deftest p45
  (is (not (pair? :pair))))

(deftest p46
  (is (not (pair? :pear))))

(deftest p47
  (is (pair? (pair :pear nil))))        ; NB

(deftest p51
  (is (= (llist [:split] :pea)
         (lcons [:split] :pea))))

(deftest p52
  (is (= [(llist '_.0 '_.1 :salad)]
       (run* [r]
             (exist [x y]
                    (== (lcons x (lcons y :salad)) r))))))

(deftest p54
  (is (= [true]
       (run* [q]
             (pairo (lcons q q))
             (== true q)))))

(deftest p55
  (is (== []
       (run* [q]
             (pairo [])
             (== true q)))))

(deftest p56
  (is (== []
       (run* [q]
             (pairo :pair)
             (== true q)))))

(deftest p57
  (is (== [(lcons '_.0 '_.1)]
       (run* [x]
             (pairo x)))))

(deftest p58
  (is (== [(lcons '_.0 :pear)]
       (run* [r]
             (pairo (lcons r :pear))))))