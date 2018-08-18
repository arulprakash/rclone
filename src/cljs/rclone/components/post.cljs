(ns rclone.components.post
  (:require [re-frame.core :refer [reg-sub]]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [rclone.db :as db]
            [rclone.semanticui :as sui]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx]]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [day8.re-frame.http-fx]
            [cljs-drag-n-drop.core :as dnd]
            [ajax.core :refer [GET POST json-response-format json-request-format url-request-format]]))
;;Local state

(def post-db
  (r/atom
   {:group ""
    :url   ""
    :title ""
    :image ""
    :text  ""
    :active-tab "post"
    :drop-text "Drop files here"}))

;;Subscriptions
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
        [:> sui/formdd {:placeholder "Choose the Sub-Reddit to post"
                        :on-search-change (fn [e data] (swap! post-db assoc :group (-> data .-value)))
                        :options     user-subs}]))}))
(defn post-tab
  [tab]
  (let [active-tab (:active-tab @post-db)]
    [:> sui/menuitm {:name tab
                     :active (if (= active-tab tab)
                               true
                               false)
                     :on-click (fn [e data]
                                 (swap! post-db assoc
                                        :active-tab (-> data .-name)))}]))
(defn drop-canvas
  []
  (r/create-class
   {:display-name "drop-file"
    :reagent-render
    (fn [this]
      [:div {:id "drop-file"
             :draggable true
             :style {:border "2px dashed"
                     :border-radius "2px"
                     :padding "25px"
                     :text-align :center}}
       (:drop-text @post-db)])
    :component-did-mount
    (fn [this]
      (dnd/subscribe! (js/document.querySelector "#drop-file") :unique-key
                      {:start (fn [e] (println "d1 Start"))
                       :enter (fn [e] (println "d1 Enter"))
                       :drop  (fn [e files]
                                (let [file (-> files (.item 0))
                                      name (.-name file)
                                      path (.-path (-> file .getAsFile))]
                                  (.preventDefault e)
                                  (.stopPropagation e)
                                  (swap! post-db assoc :drop-text (str path "\\" name))))
                       :leave (fn [e] (println "d1 leave"))
                       :end   (fn [e] (println "d1 end")) }))}))
(defn post-form
  []
  [:> sui/form {:method "GET"}
   [:> sui/forminp {
                    :icon          "header"
                    :icon-position "left"
                    :value         (:title @post-db)
                    :on-change     #(swap! post-db assoc
                                           :title (-> % .-target .-value))
                    :placeholder   "Title"}]
   [:> sui/forminp {:icon          "external share"
                    :value         (:url @post-db)
                    :on-change     #(swap! post-db assoc
                                           :url (-> % .-target .-name))
                    :icon-position "left"
                    :placeholder   "URL"}]
   [:> sui/formbtn {:type     "submit"
                    :value    "Post"
                    :on-click #(rf/dispatch [:post])}
    "Post"]])

(defn image-form
  []
  [:> sui/form {:method "GET"}
   [:> sui/forminp {
                    :icon          "header"
                    :icon-position "left"
                    :value         (:title @post-db)
                    :on-change     #(swap! post-db assoc
                                           :title (-> % .-target .-value))
                    :placeholder   "Title"}]
   [:> sui/forminp {:icon          "image"
                    :icon-position "left"
                    :type          "file"
                    :on-change     #(swap! post-db assoc
                                           :image (-> % .-target .-value))}]
   [drop-canvas]
   [:> sui/formbtn {:type     "submit"
                    :value    "Post"
                    :on-click #(rf/dispatch [:post])}
    "Post"]])

(defn link-form
  []
  [:> sui/form {:method "GET"}
   [:> sui/forminp {
                    :icon          "header"
                    :icon-position "left"
                    :value         (:title @post-db)
                    :on-change     #(swap! post-db assoc
                                           :title (-> % .-target .-value))
                    :placeholder   "Title"}]
   [:> sui/forminp {:icon          "external share"
                    :value         (:url @post-db)
                    :on-change     #(swap! post-db assoc
                                           :url (-> % .-target .-name))
                    :icon-position "left"
                    :placeholder   "URL"}]
   [:> sui/formbtn {:type     "submit"
                    :value    "Post"
                    :on-click #(rf/dispatch [:post])}
    "Post"]])

(defn post-link
  []
  [:div
   [:> sui/header {:as :h2} "Create a Post"]
   [subs-component]
   [:> sui/menu {:attached :top
                 :tabular true
                 :compact true}
    [post-tab "post"]
    [post-tab "image"]
    [post-tab "link"]]
   (case (:active-tab @post-db)
     "post" [post-form]
     "image" [image-form]
     "link" [link-form])])
