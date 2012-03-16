(ns midje.cljs.logging
  (:import (java.io BufferedWriter)
            (java.io FileWriter)))

(def ^:dynamic *file-name* "/home/ckirkendall/Development/clojure/macro-log.txt")
  
  
(defn log [message]
  (with-open [wtr (BufferedWriter. (FileWriter. *file-name* true))]
    (.write wtr message)
    (.newLine wtr)))
