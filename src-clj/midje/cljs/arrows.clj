(ns midje.cljs.arrows)


(def assert-arrows {'=> (symbol "js/assertEquals")
                    '=not=> (symbol "js/assertNotEquals")})
(def fake-arrows {'=> (symbol ".$returns")})
(def fake-mods {:times (symbol ".$times")})