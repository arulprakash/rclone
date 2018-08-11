(ns rclone.components.post
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

(def post-db
  (r/atom
   {:group ""
    :url   ""
    :title ""
    :image ""
    :text  ""}))

;;Subsrciptions
(reg-sub
 :user-subs
 (fn [db _]
   (into []
         (map #(hash-map :value (:id %) :text (:id %)) (:subscriptions db)))))

;;Handlers
(reg-event-fx
 :get-user-subs
 (fn [{:keys [db]} _]
   {:db       db
    :dispatch [:query-server
               [:subscriptions {:id (get-in db [:user :id])}
                [:id]]]}))

(reg-event-fx
 :post
 (fn [{:keys [db]} _]
   {:db       db
    :dispatch [:mutate-server
               [:post {:url         (:url @post-db)
                       :title       (:title @post-db)
                       :description (:text @post-db)}
                [:id]]]}))

;;Views
(defn subs-component
  []
  (r/create-class
   {:display-name "subs-dropdown"
    :component-will-mount
    (fn [this]
      (rf/dispatch [:get-user-subs]))
    :reagent-render
    (fn [this]
      (let [user-subs @(rf/subscribe [:user-subs])]
        [:> sui/formdd {;;:placeholder "Post to Group "
                        ;;:value (hash-map :text (:group @post-db))
                        :search :true
                        :selection :true
                        :on-search-change (fn [e data] (swap! post-db assoc :group (-> data .-value)))
                        :options     user-subs}]))}))

(defn post-link
  []
  [:div
   [subs-component]
   [:> sui/form {:method "GET"}
    [:> sui/forminp {:icon          "external share"
                     :value         (:url @post-db)
                     :on-change     #(swap! post-db assoc
                                            :url (-> % .-target .-value))
                     :icon-position "left"
                     :placeholder   "URL"}]
    [:> sui/forminp {:icon          "image"
                     :icon-position "left"
                     :type          "file"
                     :on-change     #(swap! post-db assoc
                                            :image (-> % .-target .-value))}]
    [:> sui/forminp {
                     :icon          "header"
                     :icon-position "left"
                     :value         (:title @post-db)
                     :on-change     #(swap! post-db assoc
                                            :title (-> % .-target .-value))
                     :placeholder   "Title"}]
    [:> sui/formbtn {:type     "submit"
                     :value    "Post Link"
                     :on-click #(rf/dispatch [:post])}
     "Post Link"]]])
