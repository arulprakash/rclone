(ns rclone.routes.home
  (:require [rclone.layout :as layout]
            [rclone.db.core :as db] 
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.util :as util]
            [com.walmartlabs.lacinia :refer [execute]]
            [clojure.edn :as edn]
            [clj-time.local :as local]            ))
(defn home-page []
  (layout/render "home.html"))

(defn get-user
  [context arguments value]
  (let [{:keys [id]} arguments]
    (db/get-user {:id id})))

(defn get-user-comments
  [context arguments value]
  (let [{:keys [id]} arguments]
    (db/get-user-comments {:id id})))

(defn get-user-posts
  [context arguments value]
  (let [{:keys [id]} arguments]
    (db/get-user-posts {:id id})))

(defn get-user-subs
  [context arguments value]
  (let [{:keys [id]} arguments]
    (db/get-user-subs {:id id})))

(defn get-comments
  [context arguments value]
  (let [{:keys [id]} arguments]
    (db/get-user {:id id})))

(defn create-user
  [context arguments value]
  (let [{:keys [id first_name last_name email pass]} arguments
        admin false
        created (local/local-now)]
    (db/create-user! {:id         id
                      :first_name first_name
                      :last_name  last_name
                      :email      email
                      :pass       pass
                      :created    created
                      :admin      admin
                      :is_active  false})))

(defn update-user
  [context arguments value]
  (let [{:keys [id first_name last_name email pass]} arguments
        admin false
        created (local/local-now)
        args {:id         id
              :first_name first_name
              :last_name  last_name
              :email      email
              :pass       pass
              :created    created
              :admin      admin
              :is_active  false}]
    (if (= 1 (db/update-user! args))
      {:status "Updated"}
      {:status "Update Failed"})))

(defn delete-user
  [context arguments value]
  (let [{:keys [id]} arguments]
    (if (= 1 (db/delete-user! {:id id}))
      {:status "Deleted"}
      {:status "Not Deleted"})))


(defn get-top-hosts
  [context arguments value]
  (let [{:keys [id]} arguments]
    (db/get-top-posts)))

(defn compile-schema
  []
  (-> (io/resource "edn/schema.edn")
      slurp
      edn/read-string
      (util/attach-resolvers {:get-user          get-user
                              :create-user       create-user
                              :get-user-comments get-user-comments
                              :get-user-posts    get-user-posts
                              :get-user-subs     get-user-subs
                              :get-comments      get-comments
                              :delete-user       delete-user
                              :top-posts         get-top-hosts})
      schema/compile))

(def compiled-schema (compile-schema))

(defroutes home-routes
  (GET "/" []
       (home-page)) 
  (GET "/docs" []
       (-> (response/ok (-> "docs/docs.md" io/resource slurp))
           (response/header "Content-Type" "text/plain; charset=utf-8"))))
