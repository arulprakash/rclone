(ns rclone.components.comment
  (:require [re-frame.core :refer [reg-sub]]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [rclone.db :as db]
            [rclone.semanticui :as sui]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [day8.re-frame.http-fx]
            [ajax.core :refer [GET POST json-response-format json-request-format url-request-format]]))

;;Local state

(def comment-db
  (r/atom
   {:description ""
    :posted-to ""
    :reply-to ""}))

;;Subsrciptions

;;Handlers
(reg-event-fx
 :comment
 (fn [{:keys [db]} _]
   {:db db
    :dispatch [:mutate-server
               [:comment {:url (:url @comment-db)
                          :title (:title @comment-db)
                          :description (:text @comment-db)}
                [:id]]]}))

;;Views
(defn comment-link
  []
  [:> sui/form {:method "GET"}
   [:> sui/forminp {:icon "external share"
                    :value (:url @comment-db)
                    :on-change #(swap! comment-db assoc
                                       :url (-> % .-target .-value))
                    :icon-position "left"
                    :placeholder "URL"}]
   [:> sui/forminp {:icon "image"
                    :icon-position "left"
                    :type "file"
                    :on-change #(swap! comment-db assoc
                                       :image (-> % .-target .-value))}]
   [:> sui/forminp {
                    :icon "header"
                    :icon-position "left"
                    :value (:title @comment-db)
                    :on-change #(swap! comment-db assoc
                                       :title (-> % .-target .-value))
                    :placeholder "Title"}]
   [:> sui/formbtn {:type "submit"
                    :value "Post Link"
                    :on-click #(rf/dispatch [:comment])}
    "Comment"]])
