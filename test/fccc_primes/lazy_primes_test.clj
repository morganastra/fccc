(ns fccc-primes.lazy-primes-test
  (:require [fccc-primes.lazy-primes :refer :all]
            [clojure.data.priority-map :as pm]
            [clojure.test :refer :all]))

(def some-primes [2 3 5 7 11 13 17 19 23 29 31 37 41 43 47
                  53 59 61 67 71 73 79 83 89 97 101 103 107 109 113
                  127 131 137 139 149 151 157 163 167 173 179 181 191 193 197
                  199 211 223 227 229 233 239 241 251 257 263 269 271 277 281
                  283 293 307 311 313 317 331 337 347 349 353 359 367 373 379
                  383 389 397 401 409 419 421 431 433 439 443 449 457 461 463
                  467 479 487 491 499 503 509 521 523 541])

(def fixture-table-3 (pm/priority-map 3 9))
(def fixture-table-13 (pm/priority-map 3 15, 5 25, 7 49, 11 121, 13 169))

(deftest test-insert
  (testing "sieve table inserting primes"
    (let [[p table] (-> fixture-table-3 (insert-prime 5))]
      (is (= 5 p))
      (is (= [3 9] (first table)))
      (is (= [5 25] (second table))))))

(deftest test-next-composite
  (is (= 9 (next-composite fixture-table-3)))
  (is (= 15 (next-composite fixture-table-13))))

(deftest test-adjust
  (is (= [3 21] (first (adjust fixture-table-13 15)))))

(deftest test-sieve
  (let [state (sieve [13 fixture-table-13])]
    (is (= 17 (first state)))
    (is (= [3 21] (-> state second first)))
    (is (= [17 289] (-> state second last)))))

(deftest test-lazy-primes
  (is (= (first some-primes)
         (first (lazy-primes))))
  (is (= (second some-primes)
         (second (lazy-primes))))
  (is (= (take 99 some-primes)
         (lazy-primes 99))))
