(defproject fccc-primes "0.1.0-SNAPSHOT"
  :description "A command-line tool for printing multiplication tables of primes."
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/data.priority-map "0.0.7"]  ]
  :main fccc-primes.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
