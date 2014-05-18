(defproject smgui "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}

  :min-lein-version "2.3.4"

  ;; We need to add src/cljs too, because cljsbuild does not add its
  ;; source-paths to the project source-paths
  :source-paths ["src/clj" "src/cljs"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2202"]
                 [org.clojure/core.async "0.1.301.0-deb34a-alpha"]
                 [om "0.6.2"]
                 [speclj "3.0.2"]]

  :plugins [[lein-cljsbuild "1.0.3"]
            [speclj "2.5.0"]
            [jarohen/simple-brepl "0.1.0"]]

  :test-paths ["spec"]

  :cljsbuild ~(let [run-specs ["bin/speclj" "target/tests.js"]]
              { :builds { :test { :source-paths ["src/cljs" "spec/cljs"]
                                  :compiler {:output-to "target/tests.js"
                                             :pretty-print true}
                                  :notify-command run-specs}

                          :dev { :source-paths ["src/cljs"]
                                 :compiler { :output-to "public/js/smgui.js"
                                             :optimizations :whitespace
                                             :pretty-print true}}}
                :test-commands {"test" run-specs}}))