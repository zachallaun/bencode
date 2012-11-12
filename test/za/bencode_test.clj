(ns za.bencode-test
  (:refer-clojure :exclude [num list])
  (:use [za.bencode]
        [za.test-util]
        [midje.sweet]))

(def byte? (partial instance? java.lang.Byte))

(fact "sequence of bytes from a string"
      (byteseq "ascii") => (allfn seq? (partial every? byte?)))

(fact "about simple unfold"
      (unfold #(= % :stop-unfold)
              (fn [xs]
                (if (seq xs)
                  [(first xs) (rest xs)]
                  :stop-unfold))
              [1 2 3])
      => [[1 2 3] []])

(fact "about read-chars"
      (read-chars [\1 \3 \3 \7]) => 1337
      (read-chars [\- \1 \3 \3 \7]) => -1337
      (read-chars [\a \b \c]) => 'abc)

(fact "about with-sign"
      (with-sign [\1]) => [\+ [\1]]
      (with-sign [\- \1]) => [\- [\1]])

(fact "about digit parsing"
      (digit [\1 \f \o \o]) => [\1 [\f \o \o]]
      (digit [\1]) => [\1 nil]
      (digit [\f \o \o]) => nil?)

(fact "about number parsing"
      (num [\1 \2 \3 \f \o \o]) => [123 [\f \o \o]]
      (num [\- \1 \2 \3 \f \o \o]) => [-123 [\f \o \o]]
      (num [\f \1 \2 \3]) => nil?)

(fact "about integer decoding"
      (decode "i123e") => [123 nil]
      (decode "i1337ei123e") => [1337 [\i \1 \2 \3 \e]]
      (decode "i-1337e") => [-1337 nil])

(fact "about poorly-formed integer decoding"
      (decode "i123xe") => nil?)

(fact "about list decoding"
      (decode "li1ei2ei3ee") => [[1 2 3] nil]
      (decode "li-1ei-2ei-3ee") => [[-1 -2 -3] nil]
      (decode "li1ei2ei3eexxx") => [[1 2 3] [\x \x \x]]
      (decode "ld1:ai0eee") => [[{"a" 0}] nil])

(fact "about poorly-formed list decoding"
      (decode "li1ei2ei3exe") => nil?)

(fact "about string decoding"
      (decode "3:foo") => ["foo" ()]
      (decode "3:foobar") => ["foo" [\b \a \r]])

(fact "about poorly-formed string decoding"
      (decode "3x:foo") => nil?)

(fact "about dictionary decoding"
      (decode "d1:ai0e1:bi1ee") => [{"a" 0 "b" 1} nil]
      (decode "d1:ali0ei1ei2eee") => [{"a" [0 1 2]} nil])

(fact "TODO: Write encoding tests"
      true => false)
