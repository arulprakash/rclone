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
            [clj-time.local :as local]
            [clojure.zip :as zip]))
(def resolvers (transient []))

(defn home-page []
  (layout/render "home.html"))

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

(defn create-post
  [context arguments value]
  (let [{:keys [title url description created]} arguments
        admin false
        created (local/local-now)]
    (into {} (db/create-post! {:title title
                               :url url
                               :description description
                               :created created
                               :posted_by 100
                               :posted_in 1}))))

(defn get-top-posts
  [context arguments value]
  (let [{:keys [id]} arguments]
    (db/get-top-posts)))

(defn upvote-post
  [context arguments value]
  (let [{:keys [id]} arguments]
    (db/upvote-post! {:id id})))

(defn downvote-post
  [context arguments value]
  (let [{:keys [id]} arguments]
    (db/downvote-post! {:id id})))

(defn create-comment
  [context arguments value]
  (let [{:keys [description votes created posted_to replied_to commented_by]} arguments
        created (local/local-now)]
    (into {} (db/create-comment! {:description description
                                  :votes 1
                                  :created created
                                  :posted_to posted_to
                                  :replied_to replied_to
                                  :commented_by commented_by}))))

(defn execute-crud
  [context arguments value]
  (let [{:keys [id]} arguments]
    (db/get-top-posts)))

(defn get-user
  [context arguments value]
  (let [{:keys [id pass]} arguments]
    (db/get-user {:id id :pass pass})))

(defn ^:private keyword-factory
  [keyword]
  (fn [context arguments value]
    (let [f @(resolve (symbol (name keyword)))]
      #(f context arguments value))))

(defn map-zipper [m]
  (zip/zipper 
   (fn [x] (or (map? x) (map? (nth x 1))))
   (fn [x] (seq (if (map? x) x (nth x 1))))
   (fn [x children] 
     (if (map? x) 
       (into {} children) 
       (assoc x 1 (into {} children))))
   m))

(defn get-keys-tree
  "Supply map zipper of tree and key to search"
  [tz]
  (if (not (zip/end? tz))
    (let [node (first tz)]
      (when (= (first node) :resolve)
        (conj! resolvers (second node)))
      (recur (zip/next tz)))
    (persistent! resolvers)))

(defn get-resolvers
  []
  (as-> (io/resource "edn/schema.edn") c
    (slurp c)
    (edn/read-string c)
    (map-zipper c)
    (get-keys-tree c)
    (map #(assoc {} % @(resolve (symbol (name %)))) c)
    (into {} c)))

(defn compile-schema
  []
  (let [resolvers (get-resolvers)]
    (as-> (io/resource "edn/schema.edn") c
      (slurp c)
      (edn/read-string c)
      (util/attach-resolvers c resolvers)
      (schema/compile c)
      )))

(def compiled-schema (compile-schema))

(defroutes home-routes
  (GET "/" []
       (home-page))
  (GET "/docs" []
       (-> (response/ok (-> "docs/docs.md" io/resource slurp))
           (response/header "Content-Type" "text/plain; charset=utf-8"))))
