(ns rclone.handlers
  (:require [rclone.db :as db]
            [ajax.core :refer [GET POST json-response-format json-request-format]]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]
            [day8.re-frame.http-fx]))

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
 :graphql-handler
 (fn [{:keys [db]} _]
   {    :http-xhrio {:method          :get
                     :uri             "/graphql"
                     :params           {"query"  "{user(id:\"arul\") {first_name}}"}
                     :response-format (json-response-format {:keywords? true})
                     :on-success      [:good-http-result]
                     :on-failure      [:bad-http-result]}}))

;;:format  {:content-type "application/graphql"}

(GET "/graphql" {:params {"query"  "{user(id:\"arul\") {first_name}}"}})
