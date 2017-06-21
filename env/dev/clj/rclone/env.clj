(ns rclone.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [rclone.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[rclone started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[rclone has shut down successfully]=-"))
   :middleware wrap-dev})
