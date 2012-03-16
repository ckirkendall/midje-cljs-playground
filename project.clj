(defproject goog-test "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :source-path "src-clj"
  :dependencies [[org.clojure/clojure "1.3.0"]]
  :cljsbuild {
    :builds [{:source-path "src-cljs"
              :compiler {:output-to "resources/public/js/main.js"
                         :optimizations :whitespace
                         :pretty-print true}}]})
