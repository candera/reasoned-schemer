(ns reasoned-schemer.main
  (:require reasoned-schemer.chapter1
            reasoned-schemer.chapter2
            reasoned-schemer.chapter3)
  (:use clojure.test)
  (:gen-class))

(defn -main []
 (run-all-tests #"reasoned-schemer\.chapter.*"))