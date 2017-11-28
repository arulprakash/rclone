(defproject rclone "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[buddy "1.3.0"]
                 [cider/cider-nrepl "0.15.0-SNAPSHOT"]
                 [clj-time "0.14.0"]
                 [cljs-ajax "0.7.2"]
                 [compojure "1.6.0"]
                 [conman "0.6.8"]
                 [cprop "0.1.11"]
                 [funcool/struct "1.1.0"]
                 [luminus-immutant "0.2.3"]
                 [luminus-migrations "0.4.2"]
                 [luminus-nrepl "0.1.4"]
                 [luminus/ring-ttl-session "0.3.2"]
                 [markdown-clj "1.0.1"]
                 [metosin/muuntaja "0.3.2"]
                 [metosin/ring-http-response "0.9.0"]
                 [mount "0.1.11"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.908" :scope "provided"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.clojure/tools.logging "0.4.0"]
                 [org.postgresql/postgresql "42.1.3"]
                 [org.webjars.bower/tether "1.4.0"] 
                 [org.webjars/font-awesome "4.7.0"]
                 [org.webjars/webjars-locator-jboss-vfs "0.1.0"]
                 [re-frame "0.10.1"]
                 [day8.re-frame/http-fx "0.1.4"]
                 [reagent "0.7.0"]
                 [reagent-utils "0.2.1"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-core "1.6.2"]
                 [ring/ring-defaults "0.3.1"]
                 [secretary "1.2.3"]
                 [selmer "1.11.1"]
                 [com.walmartlabs/lacinia "0.19.0"]
                 [org.clojure/data.json "0.2.6"]
                 [cljs-react-material-ui "0.2.48"]
                 [cljsjs/semantic-ui-react "0.73.0-0"]
                 [vincit/venia "0.2.3"]
                 [clojure-future-spec "1.9.0-beta4"]
                 [org.clojure/test.check "0.9.0"]]

  :min-lein-version "2.0.0"

  :jvm-opts ["-server" "-Dconf=.lein-env"]
  :source-paths ["src/clj" "src/cljc" "src/cljs" "env/dev/cljs"]
  :test-paths ["test/clj"]
  :resource-paths ["resources" "target/cljsbuild"]
  :target-path "target/%s/"
  :main ^:skip-aot rclone.core
  :migratus {:store :database :db ~(get (System/getenv) "DATABASE_URL")}

  :plugins [[lein-cprop "1.0.3"]
            [migratus-lein "0.4.9"]
            [org.clojars.punkisdead/lein-cucumber "1.0.5"]
            [lein-cljsbuild "1.1.5"]
            [lein-immutant "2.1.0"]
            [lein-auto "0.1.2"]
            [lein-kibit "0.1.2"]
            [lein-environ "1.0.0"]]
  :cucumber-feature-paths ["test/clj/features"]


  :clean-targets ^{:protect false}
  [:target-path [:cljsbuild :builds :app :compiler :output-dir] [:cljsbuild :builds :app :compiler :output-to]]
  :figwheel
  {:http-server-root "public"
   :nrepl-port       7002
   :css-dirs         ["resources/public/css"]
   :nrepl-middleware
   [cemerick.piggieback/wrap-cljs-repl]}


  :profiles
  {:uberjar       {:omit-source    true
                   :prep-tasks     ["compile" ["cljsbuild" "once" "min"]]
                   :cljsbuild
                   {:builds
                    {:min
                     {:source-paths ["src/cljc" "src/cljs" "env/prod/cljs"]
                      :compiler
                      {:output-to     "target/cljsbuild/public/js/app.js"
                       :optimizations :advanced
                       :pretty-print  false
                       :closure-warnings
                       {:externs-validation :off :non-standard-jsdoc :off}
                       :externs       ["react/externs/react.js"]}}}}


                   :aot            :all
                   :uberjar-name   "rclone.jar"
                   :source-paths   ["env/prod/clj"]
                   :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev   {:dependencies   [[prone "1.1.4"]
                                    [ring/ring-mock "0.3.0"]
                                    [ring/ring-devel "1.6.1"]
                                    [pjstadig/humane-test-output "0.8.2"]
                                    [binaryage/devtools "0.9.4"]
                                    [clj-webdriver/clj-webdriver "0.7.2"]
                                    [com.cemerick/piggieback "0.2.2"]
                                    [doo "0.1.7"]
                                    [figwheel-sidecar "0.5.14"]
                                    [org.apache.httpcomponents/httpcore "4.4"]
                                    [org.clojure/core.cache "0.6.3"]
                                    [midje "1.8.3"]
                                    [re-frisk "0.5.0"]
                                    [org.seleniumhq.selenium/selenium-server "2.48.2" :exclusions [org.bouncycastle/bcprov-jdk15on org.bouncycastle/bcpkix-jdk15on]]]
                   :plugins        [[com.jakemccrary/lein-test-refresh "0.19.0"]
                                    [lein-doo "0.1.7"]
                                    [lein-figwheel "0.5.14"]
                                    [org.clojure/clojurescript "1.9.562"]]
                   :cljsbuild
                   {:builds
                    {:app
                     {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
                      :figwheel     {:on-jsload "rclone.core/mount-components"}
                      :compiler
                      {:main          "rclone.app"
                       :asset-path    "/js/out"
                       :output-to     "target/cljsbuild/public/js/app.js"
                       :output-dir    "target/cljsbuild/public/js/out"
                       :source-map    true
                       :preloads [re-frisk.preload]
                       :optimizations :none
                       :pretty-print  true}}}}



                   :doo            {:build "test"}
                   :source-paths   ["env/dev/clj"]
                   :resource-paths ["env/dev/resources"]
                   :repl-options   {:init-ns user
                                    :timeout 180000}
                   :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]
                   :injections     [(require 'pjstadig.humane-test-output)
                                    (pjstadig.humane-test-output/activate!)]}
   :project/test  {:resource-paths ["env/test/resources"]
                   :cljsbuild
                   {:builds
                    {:test
                     {:source-paths ["src/cljc" "src/cljs" "test/cljs"]
                      :compiler
                      {:output-to     "target/test.js"
                       :main          "rclone.doo-runner"
                       :optimizations :whitespace
                       :pretty-print  true}}}}

                   }
   :profiles/dev  {}
   :profiles/test {}}
  :env {:squiggly {:checkers [:eastwood :kibit]}})
