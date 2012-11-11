(ns za.test-util
  (:use [midje.sweet]))

(defn allfn
  "Higher-order and."
  [predicate & more]
  (fn [& args]
    (loop [pred predicate more more]
      (when-let [result (apply pred args)]
        (if-not (seq more)
          result
          (recur (first more) (rest more)))))))

(fact "about allfn"
      ((allfn string? #(> (count %) 0)) "foo") => truthy
      ((allfn string? #(> (count %) 0)) "") => falsey
      ((allfn string? #(> (count %) 0)) [:foo]) => falsey
      ((allfn string?) "foo") => truthy
      ((allfn #(> (count %&) 1)) :foo :bar) => truthy)
