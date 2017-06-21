(ns user
  (:require [mount.core :as mount]
            [rclone.figwheel :refer [start-fw stop-fw cljs]]
            rclone.core))

(defn start []
  (mount/start-without #'rclone.core/repl-server))

(defn stop []
  (mount/stop-except #'rclone.core/repl-server))

(defn restart []
  (stop)
  (start))


