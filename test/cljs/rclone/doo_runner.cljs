(ns rclone.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [rclone.core-test]))

(doo-tests 'rclone.core-test)

