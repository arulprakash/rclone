(ns rclone.db)

(def default-db
  {:page :home
   :signed-in? nil
   :show-login? false
   :sign-up? false
   :top_posts {}
   :Posts []
   :Users []
   :Groups []
   :Subscriptions []
   :Privileges []
   :error {}})
