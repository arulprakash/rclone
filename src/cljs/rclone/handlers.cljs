(ns rclone.handlers
  (:require [rclone.db :as db]
<<<<<<< HEAD
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]))
=======
            [ajax.core :refer [GET POST json-response-format json-request-format url-request-format]]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]
            [day8.re-frame.http-fx]))
>>>>>>> 06ecdbd76684ac65f43843f12ec06ff85829db7e

(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(reg-event-db
  :set-active-page
  (fn [db [_ page]]
    (assoc db :page page)))

(reg-event-db
<<<<<<< HEAD
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
=======
 :set-docs
 (fn [db [_ docs]]
   (assoc db :docs docs)))

(reg-event-db
 :good-http-result
 (fn [db [_ result]]
   (js/console.log "Success")))

(reg-event-db
 :bad-http-result
 (fn [db [_ result]]
   (js/console.log "Failure")))

(reg-event-fx
 :graphql-handler
 (fn [{:keys [db]} _]
   {:http-xhrio {:method          :get
                 :uri             "/graphql"
                 :params          {"query" "{user(id:\"arul\") {first_name}}"}
                 :format          (url-request-format)
                 :response-format (json-response-format {:keywords? true})
                 :on-success      [:good-http-result]
                 :on-failure      [:bad-http-result]}}))
>>>>>>> 06ecdbd76684ac65f43843f12ec06ff85829db7e
