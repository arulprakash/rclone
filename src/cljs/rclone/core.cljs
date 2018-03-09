(ns rclone.core
  (:require   [reagent.core :as r]
              [re-frame.core :as rf] 
              [secretary.core :as secretary]
              [goog.events :as events]
              [goog.history.EventType :as HistoryEventType]
              [markdown.core :refer [md->html]]
              [ajax.core :refer [GET POST]]
              [rclone.ajax :refer [load-interceptors!]]
              [rclone.handlers]
              [rclone.subscriptions]
              [rclone.semanticui :as sui]
              [rclone.components.login :as login]
              [goog.object]
              [reagent.dom :as dom]
              [venia.core :as v])
  (:import goog.History))

(defn app-sidebar
  []
  (let [signed-in? @(rf/subscribe [:get-db :signed-in])]
    [:> sui/sidebar {:as sui/menu
                     :animation "push"
                     :width "very thin"
                     :visible true
                     :direction "top"
                     :icon "labeled"
                     :inverted true}
     [:> sui/menumenu {:position "right"}
      (if  (not signed-in?)
        [:> sui/menuitm {:name "signin"
                         :on-click #(rf/dispatch [:flip-login])}
         "Sign In"])
      (when signed-in?
        [:> sui/menuitm {:name "subreddits"}
         "My Subreddits"]
        [:> sui/menuitm {:name "preferences"}
         "Preferences"]
        [:> sui/menuitm {:name "logout"}
         "Logout"])]]))


(defn top-grids []
  [:> sui/container
   (let [posts @(rf/subscribe [:top-posts])]
     (when (not-empty posts)
       [:> sui/grid {:rows (count posts)
                     :columns 1}
        (map #(identity [:> sui/gridr {:key (:id %)
                                       :columns 2}
                         [:> sui/gridc {:width 1}                          
                          [:> sui/gridr
                           [:> sui/button {:icon "chevron up"
                                           :size "mini"
                                           :on-click
                                           (fn []
                                             (rf/dispatch [:mutate-server [:upvote {:id (:id %)} [:votes]]]))}]]
                          [:> sui/gridr
                           [:> sui/button {:icon "chevron down"
                                           :size "mini"
                                           :on-click (fn [] (rf/dispatch [:mutate-server [:upvote {:id (:id %)} [:votes]]]))}]]]
                         [:> sui/gridc {:width 7}                        
                          [:> sui/gridr {:key :tle}
                           [:> sui/gridc [:a {:href (:url %)} (:title %)]]]
                          [:> sui/gridr {:key :txt}
                           [:> sui/gridc (:description %)]]]]) posts)]))])

(defn home-page []
  [:div.container
   (when-let [docs @(rf/subscribe [:docs])]
     [:div.row
      [:div
       [app-sidebar]
       [top-grids]
       [login/login-modal]
       ]])])

(def pages
  {:home  #'home-page})

(defn page []
  [:div 
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:query-server [:top_posts [:id :created :changed :title :description :url :posted_by :posted_in :votes]]]) 
  (rf/dispatch [:set-active-page :home]))

(secretary/defroute "/about" []
  (rf/dispatch [:set-active-page :about]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     HistoryEventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET "/docs" {:handler #(rf/dispatch [:set-docs %])}))

(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))
