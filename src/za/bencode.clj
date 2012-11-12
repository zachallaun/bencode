(ns za.bencode)

(defn unfold
  "TODO: unfold docstring"
  [p f x]
  (loop [acc [] x x]
    (let [result (f x)]
      (if (p result)
        [acc x]
        (recur (conj acc (first result)) (second result))))))

(defn byteseq
  "Accepts a string, returns a sequence of bytes."
  [s]
  (map #(-> % int byte) s))
