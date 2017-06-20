(ns fccc-primes.core
  (:require [clojure.tools.cli :as cli]
            [fccc-primes.lazy-primes :as lp]
            [fccc-primes.render :as render])
  (:gen-class))

(def cli-options
  [["-n" "--number N" "Number of primes to use"
    :default 10
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %) "n must be greater than 0"]]
   ["-t" "--no-table" "Don't print a multiplication table, just print N primes"]
   ["-h" "--help"]])

(defn -main [& args]
  "Command-line entry point."
  (let [{:keys [options summary errors]} (cli/parse-opts args cli-options)
        {:keys [number no-table help]} options
        primes (lp/lazy-primes number)]
    (cond
      (or errors help) (render/help summary errors)
      no-table (render/primes primes)
      :else (render/table primes))))
