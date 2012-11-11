(ns za.bencode-test
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
      => [1 2 3])

(fact "about infinite unfold"
      (take 5 (unfold #(= % :stop-unfold)
                      (fn [xs]
                        (if (seq xs)
                          [(first xs) (rest xs)]
                          :stop-unfold))
                      (range)))
      => [0 1 2 3 4])
