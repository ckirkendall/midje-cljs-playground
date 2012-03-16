(ns midje.cljs.core)


(defn create-mock [sym])

(def runner (goog.testing.TestRunner.))

(def test-case (goog.testing.TestCase. "Midge ClojureScript Tests"))

(defn run-test []
  (.initialize runner test-case)
  (.execute runner))
