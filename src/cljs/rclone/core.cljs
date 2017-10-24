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
              [cljsjs.semantic-ui-react]
              [goog.object]
              [reagent.dom :as dom]
              [venia.core :as v])
  (:import goog.History))

(def semantic-ui js/semanticUIReact)

(defn component
  [k & ks]
  (if (seq ks)
    (apply goog.object/getValueByKeys semantic-ui k ks)
    (goog.object/get semantic-ui k)))

(def container (component "Container"))
(def image (component "Image"))
(def segment (component "Segment"))
(def button (component "Button"))
(def input (component "Input"))
(def label (component "Label"))
(def slist (component "List"))
(def header (component "Header"))
(def grid (component "Grid"))
(def gridc (component "Grid" "Column"))
(def gridr (component "Grid" "Row"))

(def card (component "Card"))
(def cardhdr (component "Card" "Header"))
(def cardtxt (component "Card" "Description"))
(def cardcnt (component "Card" "Content"))
(def cardgrp (component "Card" "Group"))

(defn nav-link [uri title page collapsed?]
  (let [selected-page (rf/subscribe [:page])]
    [:li.nav-item
     {:class (when (= page @selected-page) "active")}
     [:a.nav-link
      {:href     uri
       :on-click #(reset! collapsed? true)} title]]))

(defn navbar []
  (r/with-let [collapsed? (r/atom true)]
    [:nav.navbar.navbar-dark.bg-primary
     [:button.navbar-toggler.hidden-sm-up
      {:on-click #(swap! collapsed? not)} "☰"]
     [:div.collapse.navbar-toggleable-xs
      (when-not @collapsed? {:class "in"})
      [:a.navbar-brand {:href "#/"} "rclone"]
      [:ul.nav.navbar-nav
       [nav-link "#/" "Home" :home collapsed?]
       [nav-link "#/about" "About" :about collapsed?]]]]))

(defn about-page []
  [:p "Testing Semantic UI"])

(defn top-posts []
  [:> container 
   (let [posts @(rf/subscribe [:top-posts])]
     (when (not-empty posts) 
       (map #(identity [:> card {:key (:id %)}
                        [:> cardcnt
                         [:> cardhdr
                          [:a {:href (:url %)} (:title %)]]
                         [:> cardtxt (:description %)]]]) posts)))])

(defn top-grids []
  [:> container
   (let [posts @(rf/subscribe [:top-posts])]
     (when (not-empty posts)
       [:> grid {:rows (count posts)
                 :columns 1}
        (map #(identity [:> gridr {:key (:id %)
                                   :columns 2}
                         [:> gridc {:width 1}                          
                          [:> gridr
                           [:> button {:icon "chevron up"
                                       :size "mini"
                                       :on-click
                                       (fn []
                                         (rf/dispatch [:mutate-server [:upvote {:id (:id %)} [:votes]]]))}]]
                          [:> gridr
                           [:> button {:icon "chevron down"
                                       :size "mini"
                                       :on-click (fn [] (rf/dispatch [:mutate-server [:upvote {:id (:id %)} [:votes]]]))}]]]
                         [:> gridc {:width 7}                        
                          [:> gridr {:key :tle}
                           [:> gridc [:a {:href (:url %)} (:title %)]]]
                          [:> gridr {:key :txt}
                           [:> gridc (:description %)]]]]) posts)]))])

(defn home-page []
  [:div.container
   (when-let [docs @(rf/subscribe [:docs])]
     [:div.row
      [:div
       [top-grids]]])])

(def pages
  {:home  #'home-page
   :about #'about-page})

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
