(ns rclone.routes.home
  (:require [rclone.layout :as layout]
            [rclone.db.core :as db]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.util :as util]))

(defn home-page []
  (layout/render "home.html"))

(defn star-wars-schema
  []
  (-> (io/resource "schema.edn")
      slurp
      edn/read-string
      (util/attach-resolvers {:hero db/resolve-user
                              :human db/resolve-post
                              :droid db/resolve-comment
                              :friends db/resolve-subscription})
      schema/compile))

(defroutes home-routes
  (GET "/" []
       (home-page))
  (GET "/docs" []
       (-> (response/ok (-> "docs/docs.md" io/resource slurp))
           (response/header "Content-Type" "text/plain; charset=utf-8"))))

