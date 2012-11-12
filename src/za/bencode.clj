(ns za.bencode
  (:refer-clojure :exclude [num list]))

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

(defn char<=
  [& cs]
  (apply <= (map int cs)))

(defn end? [e] (= e \e))

(defn read-chars
  "Accepts a sequence of chars, concatenates them, and reads them.

  [\1 \2 \3] => 123"
  [chars]
  (read-string (apply str chars)))

(defn digit
  [[x & rest]]
  (when (and x (char<= \0 x \9))
    [x rest]))

(defn with-sign
  [[x & rest :as bin]]
  (if (or (= x \+) (= x \-))
    [x rest]
    [\+ bin]))

(defn with-end
  "Checks that unparsed characters start with char e, then returns the parse
  result and the rest.

  [parsed [e & rest]] => [parsed rest]
  [parsed [not-e]] => nil"
  [parser bin]
  (when-let [[x [e & rest]] (parser bin)]
    (when (end? e)
      [x rest])))

;;; Bencode decoding

(defmulti -decode
  (fn [bin]
    (first bin)))

;;; Integers

(defn num
  [bin]
  (let [[sign bin] (with-sign bin)
        [digits rest] (unfold nil? digit bin)]
    (when (seq digits)
      [(read-chars (cons sign digits)) rest])))

(defmethod -decode \i
  [[_ & bin]]
  (with-end num bin))

;;; Lists

(defn list
  [bin]
  (unfold nil? -decode bin))

(defmethod -decode \l
  [[_ & bin]]
  (with-end list bin))

;;; Strings

(defn string
  [bin]
  (when-let [[len more] (num bin)]
    (when-let [more (and (= \: (first more))
                         (rest more))]
      [(apply str (take len more))
       (drop len more)])))

(defmethod -decode :default
  [bin]
  (string bin))

;;; Dictionaries

(defn kv
  [bin]
  (when-let [[key more] (string bin)]
    (when-let [[val more] (-decode more)]
      [[key val] more])))

(defmethod -decode \d
  [[_ & bin]]
  (when-let [[dict more] (with-end #(unfold (complement seq) kv %) bin)]
    [(into {} dict) more]))

(defn decode
  "Decodes a bencoded byte string.

  \"i42e\" => 42
  \"5:hello\" => \"hello\"
  \"li42e3:fooe\" => (42, \"foo\")
  \"d3:fooi1e3:bari2e\" => {\"foo\" 1 \"bar\" 2}"
  [bin]
  (-decode (map char bin)))

;;; Bencoding

(defn char->byte
  [c]
  (byte (int c)))

(defprotocol Bencodable
  (encode [data]))

(extend-type String
  Bencodable
  (encode [string]
    (byteseq (str (count string) \: string))))

(extend-type Long
  Bencodable
  (encode [n]
    (byteseq (str \i n \e))))

(defn encode-sequence [s]
  (vec (reduce #(concat (-encode %2) %1) [(char->byte \e)] (reverse s))))

(defn encode-list [l]
  (cons (char->byte \l) (encode-sequence l)))

(extend-type clojure.lang.LazySeq
  Bencodable
  (encode [s]
    (encode-list s)))

(extend-type clojure.lang.PersistentVector
  Bencodable
  (encode [s]
    (encode-list s)))

(defn encode-map
  [m]
  (let [kv-seq (reduce (fn [acc [k v]] (conj acc (name k) v)) [] m)]
    (cons (char->byte \d) (encode-sequence kv-seq))))

(extend-type clojure.lang.PersistentHashMap
  Bencodable
  (encode [m]
    (encode-map m)))

(extend-type clojure.lang.PersistentArrayMap
  Bencodable
  (encode [m]
    (encode-map m)))

