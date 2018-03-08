(ns rclone.subscriptions
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :page
  (fn [db _]
    (:page db)))

(reg-sub
 :docs
 (fn [db _]
   (:docs db)))

(reg-sub
 :top-posts
 (fn [db _]
   (:top_posts db)))

(reg-sub
 :get-db
 (fn [db [_ v]]
   (get db v)))
