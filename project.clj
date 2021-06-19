(defproject io.logicblocks/kafka.connect.event-feed "0.0.1-RC1"
  :description "A Kafka Connect connector to read a HAL based event feed resource."
  :url "https://github.com/logicblocks/kafka.connect.event-feed"

  :license {:name "The MIT License"
            :url  "https://opensource.org/licenses/MIT"}

  :plugins [[lein-cloverage "1.1.2"]
            [lein-shell "0.5.0"]
            [lein-ancient "0.6.15"]
            [lein-changelog "0.3.2"]
            [lein-cprint "1.3.3"]
            [lein-eftest "0.5.9"]
            [lein-codox "0.10.7"]
            [lein-cljfmt "0.6.7"]
            [lein-kibit "0.1.8"]
            [lein-bikeshed "0.5.2"]
            [jonase/eastwood "0.3.11"]]

  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/tools.logging "1.1.0"]

                 [halboy "5.1.1"]]

  :profiles
  {:provided
   {:dependencies [[org.apache.kafka/connect-api "2.8.0"]]}
   :shared
   [:provided {:dependencies   [[org.clojure/test.check "1.1.0"]

                                [org.slf4j/jcl-over-slf4j "1.7.30"]
                                [org.slf4j/jul-to-slf4j "1.7.30"]
                                [org.slf4j/log4j-over-slf4j "1.7.30"]
                                [ch.qos.logback/logback-classic "1.2.3"]

                                [nrepl "0.8.3"]
                                [eftest "0.5.9"]

                                [camel-snake-kebab "0.4.2"]
                                [uritemplate-clj "1.3.0"]

                                [http-kit.fake "0.2.2"]]
               :resource-paths ["test-resources"]}]
   :unit
   [:shared {:test-paths ^:replace ["test/shared"
                                    "test/unit"]}]
   :integration
   [:shared {:dependencies [[org.slf4j/jcl-over-slf4j "1.7.30"]
                            [org.slf4j/jul-to-slf4j "1.7.30"]
                            [org.slf4j/log4j-over-slf4j "1.7.30"]
                            [ch.qos.logback/logback-classic "1.2.3"]

                            [io.logicblocks/kafka.testing "0.0.2"]
                            [fundingcircle/jackdaw "0.8.0"]
                            [org.sourcelab/kafka-connect-client "3.1.1"]
                            [kelveden/clj-wiremock "1.7.0"]]
             :test-paths   ^:replace ["test/shared"
                                      "test/integration"]
             :eftest       {:multithread? false}}]
   :dev
   [:unit :integration {:test-paths ^:replace ["test/shared"
                                               "test/unit"
                                               "test/integration"]}]
   :prerelease
   {:release-tasks
    [["shell" "git" "diff" "--exit-code"]
     ["change" "version" "leiningen.release/bump-version" "rc"]
     ["change" "version" "leiningen.release/bump-version" "release"]
     ["vcs" "commit" "Pre-release version %s [skip ci]"]
     ["vcs" "tag"]
     ["deploy"]]}
   :release
   {:release-tasks
    [["shell" "git" "diff" "--exit-code"]
     ["change" "version" "leiningen.release/bump-version" "release"]
     ["codox"]
     ["changelog" "release"]
     ["shell" "sed" "-E" "-i.bak" "s/\"[0-9]+\\.[0-9]+\\.[0-9]+\"/\"${:version}\"/g" "README.md"]
     ["shell" "rm" "-f" "README.md.bak"]
     ["shell" "git" "add" "."]
     ["vcs" "commit" "Release version %s [skip ci]"]
     ["vcs" "tag"]
     ["deploy"]
     ["change" "version" "leiningen.release/bump-version" "patch"]
     ["change" "version" "leiningen.release/bump-version" "rc"]
     ["change" "version" "leiningen.release/bump-version" "release"]
     ["vcs" "commit" "Pre-release version %s [skip ci]"]
     ["vcs" "tag"]
     ["vcs" "push"]]}}

  :aot [kafka.connect.event-feed.task
        kafka.connect.event-feed.connector]

  :target-path "target/%s/"
  :test-paths ["test/shared"
               "test/unit"
               "test/integration"]

  :cloverage
  {:ns-exclude-regex [#"^user"]}

  :codox
  {:namespaces  [#"^kafka\.connect\.event-feed"]
   :metadata    {:doc/format :markdown}
   :output-path "docs"
   :doc-paths   ["docs"]
   :source-uri  "https://github.com/logicblocks/kafka.connect.event-feed/blob/{version}/{filepath}#L{line}"}

  :cljfmt {:indents ^:replace {#".*" [[:inner 0]]}}

  :eastwood {:config-files ["config/linter.clj"]}

  :deploy-repositories
  {"releases"  {:url "https://repo.clojars.org" :creds :gpg}
   "snapshots" {:url "https://repo.clojars.org" :creds :gpg}}

  :aliases {"test" ["do"
                    ["with-profile" "unit" "eftest" ":all"]
                    ["with-profile" "integration" "eftest" ":all"]]})
