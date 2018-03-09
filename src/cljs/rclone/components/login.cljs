(ns rclone.components.login
  (:require [re-frame.core :refer [reg-sub]]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [rclone.db :as db]
            [rclone.semanticui :as sui]
            [re-frame.core :refer [dispatch reg-event-db reg-event-fx ->interceptor get-coeffect assoc-effect assoc-coeffect debug]]
            [day8.re-frame.http-fx]
            [ajax.core :refer [GET POST json-response-format json-request-format url-request-format]]))


;;Subsrciptions
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

;;Views
(defn login-form
  []
  [:> sui/form
   [:> sui/formfld
    [:label "Username"]
    [:input]]
   [:> sui/formfld
    [:label "Password"]
    [:input {:type "password"}]]
   [:div
    [:a {:href "todo.com"} "Reset Password"]]
   [:> sui/formbtn "Login"]])

(defn signup-form
  []
  [:> sui/form
   [:> sui/formfld
    [:label "Username"]
    [:input]]
   [:> sui/formfld
    [:label "Password"]
    [:input {:type "password"}]]
   [:> sui/formfld
    [:label "Verify Password"]
    [:input {:type "password"}]]
   [:> sui/formfld
    [:label "EMail"]
    [:input {:type "email"}]]
   [:> sui/formbtn "SIGN UP"]])

(defn login-modal
  []
  (let [show-login @(rf/subscribe [:show-login?])]
    [:> sui/modal {:size "small"
                   :open show-login
                   :on-close #(rf/dispatch [:flip-login])}
     [:> sui/modalhdr "Login"]
     [:> sui/modalcnt
      [login-form]]]))
