(defproject rclone "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[buddy "2.0.0"]
                 [clj-time "0.14.4"]
                 [cljs-ajax "0.7.4"]
                 [cljs-drag-n-drop "0.1.0"]
                 [cljs-react-material-ui "0.2.48"]
                 [cljsjs/semantic-ui-react "0.73.0-0"]
                 [com.cognitect/transit-java "0.8.332"]
                 [com.walmartlabs/lacinia "0.28.0"]
                 [compojure "1.6.1"]
                 [conman "0.8.2"]
                 [cprop "0.1.11"]
                 [day8.re-frame/http-fx "0.1.6"]
                 [funcool/struct "1.3.0"]
                 [luminus-immutant "0.2.4"]
                 [luminus-migrations "0.5.2"]
                 [luminus/ring-ttl-session "0.3.2"]
                 [markdown-clj "1.0.2"]
                 [metosin/compojure-api "1.1.12"]
                 [metosin/muuntaja "0.5.0"]
                 [metosin/ring-http-response "0.9.0"]
                 [midje "1.9.2"]
                 [mount "0.1.12"]
                 [nrepl "0.4.4"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.339" :scope "provided"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/tools.cli "0.3.7"]
                 [org.clojure/tools.logging "0.4.1"]
                 [org.postgresql/postgresql "42.2.4"]
                 [org.webjars.bower/tether "1.4.4"]
                 [org.webjars/bootstrap "4.1.3"]
                 [org.webjars/font-awesome "5.2.0"]
                 [org.webjars/webjars-locator "0.34"]
                 [re-frame "0.10.5"]
                 [reagent "0.8.1"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-defaults "0.3.2"]
                 [secretary "1.2.3"]
                 [selmer "1.11.8"]
                 [vincit/venia "0.2.3"]]

  :min-lein-version "2.0.0"
  :source-paths ["src/clj" "src/cljc" "src/cljs" "env/dev/cljs"]
  :test-paths ["test/clj"]
  :resource-paths ["resources" "target/cljsbuild"]
  :target-path "target/%s/"
  :main ^:skip-aot rclone.core
  :migratus {:store :database :db ~(get (System/getenv) "DATABASE_URL")}
  :plugins [[org.clojars.punkisdead/lein-cucumber "1.0.5"]
            [lein-cljsbuild "1.1.7"]
            [lein-immutant "2.1.0"]]
  :cucumber-feature-paths ["test/clj/features"]
  :clean-targets ^{:protect false} [:target-path [:cljsbuild :builds :app :compiler :output-dir] [:cljsbuild :builds :app :compiler :output-to]]
  :figwheel
  {:http-server-root "public"
   :nrepl-port       7002
   :css-dirs         ["resources/public/css"]
   :nrepl-middleware [cider/wrap-cljs-repl cider.piggieback/wrap-cljs-repl]}
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
                       :externs       ["react/externs/react.js"]}}}} :aot            :all
                   :uberjar-name   "rclone.jar"
                   :source-paths   ["env/prod/clj"]
                   :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev   {:jvm-opts ["-Dconf=dev-config.edn"]
                   :dependencies   [[binaryage/devtools "0.9.10"]
                                    [cider/piggieback "0.3.8"]
                                    [clj-webdriver/clj-webdriver "0.7.2"]
                                    [day8.re-frame/re-frame-10x "0.3.3-react16"]
                                    [day8.re-frame/tracing "0.5.1"]
                                    [doo "0.1.10"]
                                    [expound "0.7.1"]
                                    [figwheel-sidecar "0.5.16"]
                                    [org.apache.httpcomponents/httpcore "4.4"]
                                    [org.clojure/core.cache "0.6.3"]
                                    [org.seleniumhq.selenium/selenium-server "2.48.2" :exclusions [org.bouncycastle/bcprov-jdk15on org.bouncycastle/bcpkix-jdk15on]]
                                    [pjstadig/humane-test-output "0.8.3"]
                                    [prone "1.6.0"]
                                    [ring/ring-devel "1.6.3"]
                                    [ring/ring-mock "0.3.2"]]
                   :plugins        [[com.jakemccrary/lein-test-refresh "0.23.0"]
                                    [lein-doo "0.1.10"]
                                    [lein-figwheel "0.5.16"]]
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
                       :preloads [day8.re-frame-10x.preload]
                       :optimizations :none
                       :pretty-print  true
                       :closure-defines {"re_frame.trace.trace_enabled_QMARK_" true}}}}}
                   :doo            {:build "test"}
                   :source-paths   ["env/dev/clj"]
                   :resource-paths ["env/dev/resources"]
                   :repl-options   {:init-ns user}
                   :injections     [(require 'pjstadig.humane-test-output)
                                    (pjstadig.humane-test-output/activate!)]}
   :project/test  {:jvm-opts ["-Dconf=test-config.edn"]
                   :resource-paths ["env/test/resources"]
                   :cljsbuild
                   {:builds
                    {:test
                     {:source-paths ["src/cljc" "src/cljs" "test/cljs"]
                      :compiler
                      {:output-to     "target/test.js"
                       :main          "rclone.doo-runner"
                       :optimizations :whitespace
                       :pretty-print  true}}}}}
   :profiles/dev  {}
   :profiles/test {}})
