(defproject za.bencode "0.1.0-SNAPSHOT"
  :description "Bencode encoding and decoding."
  :url "https://github.com/zachallaun/bencode"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.flatland/ordered "1.5.2"]]

  :profiles {:dev {:plugins [[lein-midje "3.1.3"]]
                   :dependencies [[midje "1.6.3"]]}})
