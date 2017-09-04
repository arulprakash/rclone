(ns rclone.routes.home
  (:require [rclone.layout :as layout]
            [rclone.db.core :as db]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.util :as util]
            [com.walmartlabs.lacinia :refer [execute]]))

(defn home-page []
  (layout/render "home.html"))

(defn get-user
  [context arguments value]
  (let [{:keys [id]} arguments
        temp (println arguments)
        temp1 (println context)]
    (db/get-user {:id id})))

(defn get-user-comments
  []
  )

(defn get-user-posts
  [])

(defn get-user-subs
  [])

(defn create-user!
  [context arguments value]
  (let [{:keys [id]} arguments]
    ))

(defn compile-schema
  []
  (-> (io/resource "edn/schema.edn")
      slurp
      edn/read-string
      (util/attach-resolvers {:get-user get-user
                              :create-user create-user!
                              :get-user-comments get-user-comments
                              :get-user-posts get-user-posts
                              :get-user-subs get-user-subs})
      schema/compile))

(def compiled-schema (compile-schema))

(defroutes home-routes
  (GET "/" []
       (home-page))
  (GET "/docs" []
       (-> (response/ok (-> "docs/docs.md" io/resource slurp))
           (response/header "Content-Type" "text/plain; charset=utf-8"))))

