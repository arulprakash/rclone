(ns rclone.components.login
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

(def login-db
  (r/atom
   {:username  ""
    :password  ""
    :vpassword ""
    :email     ""}))

;;Subscriptions
(reg-sub
 :show-login?
 (fn [db _]
   (:show-login? db)))

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
 :login
 (fn [{:keys [db]} _]
   {:db         db
    :dispatch-n [[:query-server
                  [:user {:id   (:username @login-db)
                          :pass (:password @login-db)}
                   [:id :first_name :last_name :email :admin :is_active]]]
                 [:flip-login]
                 [:user-subs]]}))

;;Views
(defn login-form
  []
  [:> sui/form {:method "GET"}
   [:> sui/forminp {:id            "username"
                    :fluid         true
                    :icon          "user"
                    :value         (:username @login-db)
                    :on-change     #(swap! login-db assoc
                                           :username (-> % .-target .-value))
                    :icon-position "left"
                    :placeholder   "Username"}]
   [:> sui/forminp {:id            "password"
                    :fluid         true
                    :icon          "lock"
                    :icon-position "left"
                    :value         (:password @login-db)
                    :on-change     #(swap! login-db assoc
                                           :password (-> % .-target .-value))
                    :placeholder   "Password"
                    :type          "password"}]
   [:> sui/formbtn {:type     "submit"
                    :value    "send"
                    :on-click #(rf/dispatch [:login])}
    "Login"]
   [:a {:href "todo.com"} "Reset Password"]])

(defn signup-form
  []
  [:> sui/form
   [:> sui/forminp {:fluid         true
                    :icon          "user"
                    :value         (:username @login-db)
                    :on-change     #(swap! login-db assoc
                                           :username (-> % .-target .-value))
                    :icon-position "left"
                    :placeholder   "Username"}]
   [:> sui/forminp {:fluid         true
                    :icon          "lock"
                    :icon-position "left"
                    :value         (:password @login-db)
                    :on-change     #(swap! login-db assoc
                                           :password (-> % .-target .-value))
                    :placeholder   "Password"
                    :type          "password"}]
   [:> sui/forminp {:fluid         true
                    :icon          "lock"
                    :icon-position "left"
                    :value         (:vpassword @login-db)
                    :on-change     #(swap! login-db assoc
                                           :vpassword (-> % .-target .-value))
                    :placeholder   "Verify Password"
                    :type          "password"}]
   [:> sui/forminp {:fluid         true
                    :icon          "mail"
                    :icon-position "left"
                    :value         (:email @login-db)
                    :on-change     #(swap! login-db assoc
                                           :email (-> % .-target .-value))
                    :placeholder   "EMail"}
    ]
   [:> sui/formbtn {:type     "submit"
                    :on-click #(rf/dispatch [:signup])}
    "Sign Up"]])

(defn login-modal
  []
  (let [show-login @(rf/subscribe [:show-login?])]
    [:> sui/modal {:size     "small"
                   :open     show-login
                   :on-close #(rf/dispatch [:flip-login])}
     [:> sui/modalhdr "Signup/Login"]
     [:> sui/modalcnt
      [:> sui/grid {:columns 2
                    :divided true}
       [:> sui/gridr
        [:> sui/gridc
         [signup-form]]
        [:> sui/gridc
         [login-form]]]]]]))
