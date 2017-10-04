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

(def test-int
  (->interceptor
    :id :test-int
    :before (fn [context]
              (let [t2 (js/console.log "Interceptor hit")]
                context))))

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
    (js/console.log (str "Success  :  " result))))

(reg-event-db
  :bad-http-result
  (fn [db [_ result]]
    (js/console.log (str "Failure  :  " result))))

(reg-event-fx
  :query-server
  [generate-query]
  (fn [{:keys [db]} [_ query db-key]]
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
  [debug generate-query]
  (fn [{:keys [db]} [_ mutation]]
    {:http-xhrio {:method          :get
                  :uri             "/graphql"
                  :params          {"mutation" mutation}
                  :format          (url-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [:good-http-result]
                  :on-failure      [:bad-http-result]}}))
