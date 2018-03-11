(ns rclone.components.post
  (:require [re-frame.core :refer [reg-sub]]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [rclone.db :as db]
            [rclone.semanticui :as sui]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]
            [day8.re-frame.http-fx]
            [ajax.core :refer [GET POST json-response-format json-request-format url-request-format]]))
;;Local state

(def post-db
  (r/atom
   {:url ""
    :title ""
    :image ""
    :text ""}))

;;Subsrciptions
(reg-sub
 :sign-up?
 (fn [db _]
   (:sign-up? db)))

;;Handlers
(reg-event-db
 :flip-login
 (fn [db [_ _]]
   (update db :show-login? not)))

(reg-event-db
 :flip-signup
 (fn [db [_ _]]
   (update db :sign-up? not)))

(reg-event-fx
 :post
 (fn [{:keys [db]} _]
   {:db db
    :dispatch [:mutate-server
               [:post {:url (:url @post-db)
                       :title (:title @post-db)
                       :description (:text @post-db)
                       }
                [:id]]]}))

;;Views
(defn post-link
  []
  [:> sui/form {:method "GET"}
   [:> sui/forminp {:icon "external share"
                    :value (:url @post-db)
                    :on-change #(swap! post-db assoc
                                       :url (-> % .-target .-value))
                    :icon-position "left"
                    :placeholder "URL"}]
   [:> sui/forminp {:icon "image"
                    :icon-position "left"
                    :type "file"
                    :on-change #(swap! post-db assoc
                                       :image (-> % .-target .-value))}]
   [:> sui/forminp {
                    :icon "header"
                    :icon-position "left"
                    :value (:title @post-db)
                    :on-change #(swap! post-db assoc
                                       :title (-> % .-target .-value))
                    :placeholder "Title"}]
   [:> sui/formbtn {:type "submit"
                    :value "Post Link"
                    :on-click #(rf/dispatch [:post])}
    "Post Link"]])
