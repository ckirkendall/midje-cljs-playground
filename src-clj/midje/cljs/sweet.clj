(ns midje.cljs.sweet
  (:use [midje.cljs.arrows :only [assert-arrows fake-arrows fake-mods]]
        [midje.cljs.logging :only [log]]))

(defmacro provided [& forms] (fn [] "not used"))

(defn test-provs [form]
  (and (list? form) (or (= (name (first form)) "provided"))))

(defn create-fake [provs]
  (let [split (partition-by #(list? %) (rest provs)) ;TODO need a much better way to split these
        groups (partition 2 split)
        fakes (map #(concat (first %) (second %)) groups)]
    (log (str "split:" (pr-str split) "\n groups:" (pr-str groups)))
    (map #(cons (symbol "fake") %) fakes)))
    

(defn create-expects [group]
  (let [fakes (apply concat (map create-fake (second group)))
        exp (first group)]
    (log (str "fakes:::::" (pr-str fakes)))
    (cons (symbol "midje.cljs.semi-sweet/internal-expect") (concat exp fakes))))


(defmacro internal-fact [& forms]
  (let [split (partition-by test-provs forms)
        groups (partition 2 split)
        expects (map #(create-expects %) groups)]
    (log (str "split:" (pr-str split) "\n groups:" (pr-str groups) "\n exp:" (pr-str expects)))
    `(do ~@expects)))

(defmacro fact [desc & forms]
  (let [final 
        `(let [tfunc# (fn [] (internal-fact ~@forms))]
               (.add midje.cljs.core/test-case (goog.testing.TestCase.Test. ~desc  tfunc#)))]
    (log (str "final-fact:" (pr-str final)))
    final))