(ns midje.cljs.core)


(defn create-mock [sym])

(def runner (goog.testing.TestRunner.))

(def test-case (goog.testing.TestCase. "Midge ClojureScript Tests"))

(defn run-test []
  (.initialize runner test-case)
  (.execute runner))


(defn set-arg-list! [mock & args] 
  (.log js/console mock)
  (.log js/console (pr-str args))
  (let [arg-array  (apply array args)
        exp (.-$pendingExpectation mock)] 
    (set! (.-argumentList exp) arg-array)
    mock))