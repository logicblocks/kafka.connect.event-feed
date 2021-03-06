(ns kafka.connect.event-feed.version
  (:require
   [clojure.java.io :refer [reader resource]]
   [clojure.string :refer [join]])
  (:import java.io.PushbackReader))

(declare version major minor patch pre-release build string)

(let [version-file (resource "VERSION")]
  (when version-file
    (with-open [rdr (reader version-file)]
      (binding [*read-eval* false]
        (def version (read (PushbackReader. rdr)))
        (def major (:major version))
        (def minor (:minor version))
        (def patch (:patch version))
        (def pre-release (:pre-release version))
        (def build (:build version))
        (def string (str (join "." (filter identity [major minor patch]))
                      (when pre-release (str "-" pre-release))
                      (when build (str "+" build))))))))
