(ns midje.cljs.semi-sweet
  (:use [midje.cljs.arrows :only [assert-arrows fake-arrows fake-mods]]
        [midje.cljs.logging :only [log]]))


;(expect "description"
;  (call-method arg1 arg2) => result
;  (fake 
;     (call2) => test :times 2))



(defmacro fake [& form] (fn [] "not used"))


(defn fmap [mp func & args] 
      (reduce #(assoc %1 %2 (apply func (mp %2) args)) {} (keys mp)))

(defn mock-creates [fake]
  (let [fake-str (pr-str (first (second fake)))
        fake-ns (aget (.split fake-str "/") 0)
        fake-fn (aget (.split fake-str "/") 1)]
    (list (symbol "goog.testing/createMethodMock") (symbol fake-ns) (name fake-fn))))


(defn mock-mods [[sym fake]]
  (let [fake-vec (vec fake)
        fake-call (nth fake 1)
        fake-args (if (list? fake-call) (rest fake-call) '())
        args-call (cons 'midje.cljs.core/set-arg-list! fake-args) 
        fake-cnt (count fake-vec)
        fake-ret (list (fake-arrows (nth fake 2)) (nth fake 3))
        fake-mods (when (= fake-cnt 6) (list (fake-mods (nth fake 4)) (nth fake 5)))]
    (if fake-mods
      (list 'clojure.core/-> (list sym) fake-ret args-call fake-mods)
      (list 'clojure.core/-> (list sym) fake-ret args-call))))

(defn mock-verified [sym]
  (list (symbol ".$verify") sym))

(defn mock-replay [sym]
  (list (symbol ".$replay") sym))

(defn test-fakes [form]
  (log (str "test-fakes:" (prn-str form) ":" (= (name (first form)) "fake") ":" (seq? form)))
  (and (seq? form) (or (= (name (first form)) "fake")
                        (= (name (first form)) "provided"))))

(defmacro internal-expect [call arrow result & forms]
  (let [fakes (filter test-fakes forms)
        _ (log (str "##FAKE## " (pr-str fakes)))
        fake-map (reduce #(assoc %1 (gensym "fake") %2) {} fakes)
        fake-create (fmap fake-map mock-creates) 
        fake-flat (reduce #(conj (conj %1 (first %2)) (second %2)) [] fake-create)
        fake-mods (map mock-mods fake-map)
        fake-ver (map mock-verified (keys fake-map))
        fake-rep (map mock-replay (keys fake-map))
        final
    `(let ~fake-flat 
        ~@fake-mods 
        ~@fake-rep
        (~(assert-arrows arrow) ~result ~call)
        ~@fake-ver)]
    (log (str "###FINAL###" (pr-str final)))
    final))

(defmacro expect [ desc & forms]
  (let [final 
        `(let [tfunc# (fn [] (internal-expect ~@forms))]
               (.add midje.cljs.core/test-case (goog.testing.TestCase.Test. ~desc  tfunc#)))]
    (log (str "final:" (pr-str final)))
    final))