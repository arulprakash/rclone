(ns ^:figwheel-no-load rclone.app
  (:require [rclone.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
