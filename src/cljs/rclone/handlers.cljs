(ns rclone.handlers
  (:require [rclone.db :as db]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx ->interceptor get-coeffect assoc-effect assoc-coeffect debug]]
            [day8.re-frame.http-fx]
            [ajax.core :refer [GET POST json-response-format json-request-format url-request-format]]
            [venia.core :as v]))

(def generate-query
  (->interceptor
   :id :generate-query
   :before (fn [context]
             (let [params (get-in context [:coeffects :event])
                   query (v/graphql-query {:venia/queries [(second params)]})
                   t2 (js/console.log query)]
               (assoc-in context [:coeffects :event] [(first params) query])))))

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
 :assoc-db
 (fn [db [_ vec val]]
   (assoc-in db vec val)))

(reg-event-db
 :update-db
 (fn [db [_ vec f]]
   (update-in db vec f)))

(reg-event-db
 :good-http-result
 (fn [db [_ result]]
   (merge db  (:data result))))

(reg-event-db
 :bad-http-result
 (fn [db [_ result]]
   (merge db  (:error result))))

(reg-event-fx
 :query-server
 [generate-query]
 (fn [{:keys [db]} [_ query]]
   {:http-xhrio {:method          :get
                 :uri             "/graphql"
                 :temp            (js/console.log (str "In Query Server " query))
                 :params          {"query" query}
                 :format          (url-request-format)
                 :response-format (json-response-format {:keywords? true})
                 :on-success      [:good-http-result]
                 :on-failure      [:bad-http-result]}}))

(reg-event-fx
 :mutate-server
 [generate-query]
 (fn [{:keys [db]} [_ mutation]]
   {:http-xhrio {:method          :get
                 :uri             "/graphql"
                 :params          {"mutation" mutation}
                 :format          (url-request-format)
                 :response-format (json-response-format {:keywords? true})
                 :on-success      [:good-http-result]
                 :on-failure      [:bad-http-result]}}))
