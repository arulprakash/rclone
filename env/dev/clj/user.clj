(ns user
  (:require [rclone.config :refer [env]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [mount.core :as mount]
            [rclone.figwheel :refer [start-fw stop-fw cljs]]
            [rclone.core :refer [start-app]]
            [rclone.db.core]
            [conman.core :as conman]
            [luminus-migrations.core :as migrations]))

(defn start []
  (mount/start-without #'rclone.core/repl-server))

(defn stop []
  (mount/stop-except #'rclone.core/repl-server))

(defn restart []
  (stop)
  (start))

(defn restart-db []
  (mount/stop #'rclone.db.core/*db*)
  (mount/start #'rclone.db.core/*db*)
  (binding [*ns* 'rclone.db.core]
    (conman/bind-connection rclone.db.core/*db* "sql/queries.sql")))

(defn reset-db []
  (migrations/migrate ["reset"] (select-keys env [:database-url])))

(defn migrate []
  (migrations/migrate ["migrate"] (select-keys env [:database-url])))

(defn rollback []
  (migrations/migrate ["rollback"] (select-keys env [:database-url])))

(defn create-migration [name]
  (migrations/create name (select-keys env [:database-url])))



