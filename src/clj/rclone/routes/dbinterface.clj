(ns rclone.routes.dbinterface
  (:require [rclone.db.core :as db]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [clj-time.local :as local]))

(defn create-temp
  [context arguments value]
  (let [{:keys [id pass]} arguments]
    (db/get-user {:id id :pass pass})))

(defn get-user
  [context arguments value]
  (let [{:keys [id pass]} arguments]
    (db/get-user {:id id :pass pass})))

(defn get-user-posts
  [context arguments value]
  (let [{:keys [id]} arguments]
    (db/get-user-posts {:id id})))

(defn get-user-subs
  [context arguments value]
  (let [{:keys [id]} arguments]
    (->>
     (db/get-user-subs {:id id})
     (into [] (mapcat #(db/get-groups {:id (:subscribed_to %)}))))))

(defn get-comments
  [context arguments value]
  (let [{:keys [id]} arguments]
    (db/get-user {:id id})))

(defn get-user-comments
  [context arguments value]
  (let [{:keys [id pass]} arguments]
    (db/get-user {:id id :pass pass})))

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
    (db/create-post! {:title title
                      :url url
                      :description description
                      :created created
                      :posted_by 100
                      :posted_in 1})))

(defn get-top-posts
  [context arguments value]
  (let [{:keys [id]} arguments]
    (into [] (db/get-top-posts))))

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
    (db/create-comment! {:description description
                         :votes 1
                         :created created
                         :posted_to posted_to
                         :replied_to replied_to
                         :commented_by commented_by})))
