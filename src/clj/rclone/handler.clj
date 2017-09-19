(ns rclone.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [rclone.layout :refer [error-page]]
            [rclone.routes.home :refer [home-routes]]
            [rclone.routes.graphql :refer [graphql-routes]]
            [compojure.route :as route]
            [rclone.env :refer [defaults]]
            [mount.core :as mount]
            [rclone.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
   (->    #'home-routes 
          (wrap-routes middleware/wrap-csrf)
          (wrap-routes middleware/wrap-formats))   
   #'graphql-routes 
   (route/not-found
    (:body
     (error-page {:status 404
                  :title "page not found"})))))

(defn app [] (middleware/wrap-base #'app-routes))
