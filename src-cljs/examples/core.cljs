(ns examples.core
  (:require [goog.testing.jsunit :as unit]
            [goog.testing :as gt]
            [goog.testing.TestCase :as tc])
  (:use [midje.cljs.core :only [run-test]])
  (:use-macros [midje.cljs.semi-sweet :only [expect fact run-tests]]
               [midje.cljs.sweet :only [fact provided]]))

   
(defn world [] "world")

(defn world2 [] "my world")
 
(defn hello [] (str "hello " (world))) 

(defn hello2 [] (str "hello " (world) " hello " (world)))

(defn hello3 [] (str "hello " (world) " hello " (world2)))

(expect "testing1"
  (hello2) => "hello test hello test"
  (fake (examples.core/world) => "test" :times 2))
        
(fact "testing2"
  (hello2) => "hello test hello test"
  (provided (examples.core/world) => "test" :times 2)) 

(fact "testing3" ;=>should fail
  (hello2) => "hello test hello test"
  (provided (examples.core/world) => "test")) 
 
(fact "testing3" ;=>should fail
  (hello) => "hello test"
  (provided (examples.core/world) => "test" :times 2)) 
 
(fact "testing4"
  (hello2) => "hello test hello test"
  (provided (examples.core/world) => "test" :times 2)
  (hello3) => "hello test hello test2"
  (provided (examples.core/world) => "test"
            (examples.core/world2) => "test2")
  (hello) =not=> "hello world"
  (provided (examples.core/world) => "test")) 

(set! (.-onload js/window) run-test)
 


