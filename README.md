# bencode

Bencode encoding and decoding in Clojure.

## Installation
Add the following dependency to your `projecet.clj`

```
[com.zachallaun/bencode "0.1.1-SNAPSHOT"]
```

## Usage
Import into your namspace

```clj
(:require [bencode.bencode :as bencode])
```

```clj
;; `decode` works with strings or byte-arrays, and returns a pair of the
;; parsed value and the remaining, unparsed characters. (These could be
;; passed back into `decode`.)

(decode "i1337e")
;;=> [1337 nil]

(decode "i1ei2e")
;;=> [1 (\i \2 \e)]

(decode (second *1))
;;=> [2 nil]

(decode "3:foo")
;;=> ["foo" nil]

(decode "li0ei1ei2ee")
;;=> [[0 1 2] nil]

(decode "d3:fooi1e3:bari2ee")
;;=> [{"foo" 1 "bar" 2} nil]

;; to decode from file
(decode (slurp filename :encoding "ISO-8859-1"))

;; `encode` accepts a bencodable data structure and returns a byte-array.
;; For the sake of example, I'll show the return value as a string. This
;; conversion could be done with `(apply str (map char (encode xxx)))`.

(encode 1)
;;=> "i1e"

(encode -5)
;;=> "i-5e"

(encode "hello world")
;;=> "11:hello world"

(encode [1 2 3])
;;=> "li1ei2ei3ee"

(encode ["foo" "bar"])
;;=> "l3:foo3:bare"

(encode {"a" 1 "b" 2})
;;=> "d1:bi2e1:ai1ee"

(encode {:a 1 :b 2})
;;=> "d1:bi2e1:ai1ee"

(encode [{:a 1} {:b 2}])
;;=> "ld1:ai1eed1:bi2eee"

(decode (encode [1 2 3]))
;;=> [[1 2 3] nil]
```

## License

Copyright © 2012 Zach Allaun

Distributed under the Eclipse Public License, the same as Clojure.
