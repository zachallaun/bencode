# bencode

Bencode encoding and decoding in Clojure.

```clj
;; decode works with strings or byte-arrays, and returns a pair of the
;; parsed value and the remaining, unparsed characters. (These could be
;; passed back into decode.)

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

;; encode accepts a bencodable data structure and returns a byte-array.
;; For the sake of example, I'll pretend that strings are returned.

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
