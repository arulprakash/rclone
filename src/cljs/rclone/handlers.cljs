(ns rclone.handlers
  (:require [rclone.db :as db]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]))

(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(reg-event-db
  :set-active-page
  (fn [db [_ page]]
    (assoc db :page page)))

(reg-event-db
  :set-docs
  (fn [db [_ docs]]
    (assoc db :docs docs)))

(reg-event-db
  :good-http-result
  (fn [db [_ result]]
    (js/console.log result)))

(reg-event-db
  :bad-http-result
  (fn [db [_ result]]
    (js/console.log result)))

(reg-event-fx
  :get-from-graphql
  (fn [{:keys [db]} _]
    {:db         db
     :http-xhrio {:method          :get
                  :uri             "/graphql"
                  :params          "{query {user(id=\"100\") {first_name}}}"
                  :format          (ajax/json-request-format)
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [:good-http-result]
                  :on-failure      [:bad-http-result]}}))
