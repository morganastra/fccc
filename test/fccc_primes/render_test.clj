(ns fccc-primes.render-test
  (:require [fccc-primes.render :refer :all]
            [clojure.test :refer :all]))

(def fixture-primes '(2 3 5 7 11 13 17))
(def fixture-columns (cons 1 fixture-primes))

(def fixture-row-result {1 2, 2 4, 3 6, 5 10, 7 14, 11 22, 13 26, 17 34})

(def fixture-table-result '({1 2, 2 4, 3 6, 5 10, 7 14, 11 22, 13 26, 17 34}
                            {1 3, 2 6, 3 9, 5 15, 7 21, 11 33, 13 39, 17 51}
                            {1 5, 2 10, 3 15, 5 25, 7 35, 11 55, 13 65, 17 85}
                            {1 7, 2 14, 3 21, 5 35, 7 49, 11 77, 13 91, 17 119}
                            {1 11, 2 22, 3 33, 5 55, 7 77, 11 121, 13 143, 17 187}
                            {1 13, 2 26, 3 39, 5 65, 7 91, 11 143, 13 169, 17 221}
                            {1 17, 2 34, 3 51, 5 85, 7 119, 11 187, 13 221, 17 289}))

(deftest test-compute-row
  (is (= fixture-row-result
         (compute-row fixture-columns 2))))

(deftest test-compute-table
  (is (= fixture-table-result
         (compute-table fixture-columns fixture-primes))))
