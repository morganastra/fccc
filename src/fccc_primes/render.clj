(ns fccc-primes.render
  (:require [clojure.string :as s]
            [clojure.pprint :as pp]))

(defn help
  "Print the usage, and cli argument errors if there were any."
  [summary errors]
  (let [usage (s/join "\n" ["Usage:" summary])]
    (if errors
      (do
        (println "Error:")
        (println (s/join "\n" errors))
        (println)))
    (println usage)))

(defn primes
  "Just print the primes, no multiplication table.
  This is currently just an alias for println"
  [primes]
  (println primes))

(defn compute-row
  "Given the column multiplicands (including 1) and the current row
  multiplicand p, compute the current row of a multiplication table."
  [columns p]
  (into {} (map #(vector % (* p %)) columns)))

(defn compute-table
  "Given the column multiplicands (including 1) and the row
  multiplicands, compute all the rows of the multiplication table."
  [columns rows]
  (map (partial compute-row columns) rows))

(defn table
  "Format and print a multiplication table of the given primes."
  [primes]
  (let [columns (cons 1 primes)]
    (pp/print-table columns (compute-table columns primes))))
