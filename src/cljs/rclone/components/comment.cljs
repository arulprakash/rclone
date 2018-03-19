ns rclone.components.comment
:require [re-frame.core :refer [reg-sub]]
[reagent.core :as r]
[re-frame.core :as rf]


;;Local state

(def comment-db
  (r/atom
   {:description ""
    :posted-to ""
    :reply-to ""}))

;;Subsrciptions

;;Handlers
(reg-event-fx
 :post
 (fn [{:keys [db]} _]
   {:db db
    :dispatch [:mutate-server
               [:post {:url (:url @post-db)
                       :title (:title @post-db)
                       :description (:text @post-db)}
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
