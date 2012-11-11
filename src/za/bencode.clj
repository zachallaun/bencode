(ns za.bencode)

(defn unfold
  "Accepts a predicate p, a function f, and an initial value x.

  Unfold applies f to x and expects one of two kinds of values to be returned.
  The first kind is some stop-unfold value that is discovered by passing the
  value to p, which should return a truthy value when the unfold should stop.
  The second is a [y new-x] pair, where y will be accumulated and ultimately
  returned by the unfold, and new-x will be passed again into f to continue."
  [p f x]
  (lazy-seq
    (let [result (f x)]
      (when-not (p result)
        (cons (first result) (unfold p f (second result)))))))

(defn byteseq
  "Accepts a string, returns a sequence of bytes."
  [s]
  (lazy-seq
    (loop [[c & more] s]
      (when c
        (cons (-> c int byte) (byteseq more))))))
