(ns fccc-primes.lazy-primes
  (:require [clojure.data.priority-map :as pm]))

(defn insert-prime
  "Insert prime (and the first of its multiples) into the table."
  [table prime]
  [prime
   (assoc table prime (* prime prime))])

(defn next-composite
  "Return the next composite (the least by numerical value) in the table."
  [table]
  (-> table
      peek     ;; highest priority [prime composites] pair
      second)) ;; get the composite

(defn adjust
  "Assuming that candidate is a composite, iterate the composites in
  the table to remove candidate. Returns the adjusted table."
  [prev-table candidate]
  (loop [table prev-table]
    (let [[prime multiple] (first table)]
      (if (< candidate multiple)
        table
        (recur (assoc table prime (+ prime prime multiple)))))))

(defn sieve
  "Take a sieve state represented as a [prime table] vector and run the lazy
  sieve until the next prime is found (skip through as many composites as
  necessary). Return the new sieve state with the next prime."
  [[prev-prime prev-table]]
  (loop [candidate (+ 2 prev-prime)
         table prev-table]
    (if (< candidate (next-composite table))
      (insert-prime table candidate)
      (recur (+ 2 candidate) (adjust table candidate)))))

(defn lazy-primes
  "If called without an argument, returns an infinite lazy sequence of primes.
  If called with an argument n, returns a lazy sequence of the first n primes.

  See README.md for an explanation of this algorithm and its performance"
  ([]
   (let [init-seq (insert-prime (pm/priority-map) 3)]
     (cons 2 (map first (iterate sieve init-seq)))))
  ([n] (take n (lazy-primes))))
