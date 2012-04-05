(ns examples.core
  (:require [goog.testing.jsunit :as unit]
            [goog.testing :as gt]
            [goog.testing.TestCase :as tc])
  (:use [midje.cljs.core :only [run-test]])
  (:use-macros [midje.cljs.semi-sweet :only [expect fact run-tests]]
               [midje.cljs.sweet :only [fact provided]]))



(deftype WorldType [getMessage])

(def world-obj (WorldType. #(do "world")))

(defn world 
  ([] (world nil))
  ([arg] (if arg arg "world")))

(defn world2 [] "my world")
 
(defn hello  
  ([] (str "hello " (world)))
  ([arg] (str "hello " (world arg)))) 

(defn hello2 [] (str "hello " (world) " hello " (world2)))

(defn hello3 [] (str "hello " (.getMessage world-obj))) 

(expect "testing1"
  (do (hello) (hello)) => "hello test"
  (fake (examples.core/world) => "test" :times 2))
        
(fact "testing2"
  (do (hello) (hello)) => "hello test"
  (provided (examples.core/world) => "test" :times 2)) 

(fact "testing3" ;=>should fail unexpected call to world
  (do (hello) (hello)) => "hello test"
  (provided (examples.core/world) => "test")) 
 
(fact "testing3" ;=>should fail
  (hello) => "hello test"
  (provided (examples.core/world) => "test" :times 2)) 
 
(fact "testing4"
  (do (hello) (hello)) => "hello test"
  (provided (examples.core/world) => "test" :times 2)
  (hello2) => "hello test hello test2"
  (provided (examples.core/world) => "test"
            (examples.core/world2) => "test2")
  (hello) =not=> "hello world"
  (provided (examples.core/world) => "test")) 

(fact "testing5" ;testing argument validator
      (hello "test") => "hello test"
      (provided
       (examples.core/world "test") => "test"))
 
(fact "testing6" ;testing object method fake
      (hello3) => "hello test"
      (provided (.getMessage world-obj) => "test"))

(set! (.-onload js/window) run-test)
 

