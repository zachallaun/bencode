(ns za.bencode)

(defn byteseq
  "Accepts a string, returns a sequence of bytes."
  [s]
  (lazy-seq
    (loop [[c & more] s]
      (when c
        (cons (-> c int byte) (byteseq more))))))
