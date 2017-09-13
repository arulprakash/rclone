-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, first_name, last_name, email, pass, created, is_active)
VALUES (:id, :first_name, :last_name, :email, :pass, :created, :is_active)

-- :name update-user! :! :n
-- :doc update an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email, changed = :changed
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieve a user given the id.
SELECT * FROM users
WHERE id = :id

-- :name delete-user! :! :n
-- :doc delete a user given the id
DELETE FROM users
WHERE id = :id

--------------------------GROUPS-------------------------

-- :name create-group! :! :n
-- :doc creates a new group
INSERT INTO groups
(id, description, created, created_by)
VALUES (:id, :description, :created, :created_by)

-- :name update-group! :! :n
-- :doc update an existing group's description or rules
UPDATE groups
SET description = :description, rules = :rules, changed = :changed
WHERE id = :id

-- :name get-group :? :1
-- :doc retrieve a user given the id.
SELECT * FROM groups
WHERE id = :id

-- :name delete-group! :! :n
-- :doc delete a user given the id
DELETE FROM groups
WHERE id = :id

--------------------------POSTS-------------------------

-- :name create-posts! :! :n
-- :doc creates a new post
INSERT INTO posts
(id, title, url, description, created, posted_by, posted_in)
VALUES (:id, :title, :url, :description, :created, :posted_by, :posted_in)

-- :name update-posts! :! :n
-- :doc update an existing group's description or rules
UPDATE posts
SET description = :description, rules = :rules, changed = :changed
WHERE id = :id

-- :name get-user-posts :? :n
-- :doc retrieve all the posts posted by the user
SELECT * FROM posts
WHERE posted_by = :id

-- :name delete-group! :! :n
-- :doc delete a user given the id
DELETE FROM posts
WHERE id = :id
--------------------------COMMENTS-------------------------

-- :name create-comments! :! :1
-- :doc creates a new post
INSERT INTO comments
(id, description, votes, created, posted_to, replied_to)
VALUES (:id, :description, :votes, :created, :posted_to, :replied_to)

-- :name update-comments! :! :1
-- :doc update an existing group's description or rules
UPDATE comments
SET description = :description
WHERE id = :id

-- :name get-user-comments :? :n
-- :doc retrieve all the posts posted by the user
SELECT * FROM comments
WHERE posted_by = :id

-- :name get-post-comments :? :n
-- :doc retrieve all the posts posted by the user
SELECT * FROM comments
WHERE posted_in = :id

-- :name delete-comment! :! :n
-- :doc delete a user given the id
UPDATE comments
SET description = "[deleted]"
WHERE id = :id


--------------------------SUBSRIPTIONS-------------------------

-- :name create-sub! :! :1
-- :doc creates a new subscription
INSERT INTO subscriptions
(id, subscribed_to, subscriber, created)
VALUES (:id, :subscribed_to, :subscriber, :created)

-- :name get-user-subs :? :n
-- :doc retrieve all the subscriptions by an user
SELECT subscribed_to FROM subscriptions
WHERE subscriber = :id

-- :name delete-sub! :! :1
-- :doc delete a sub given the id
DELETE subscriptions
WHERE subscribed_to =:group_id AND subscriber = :user_id

--------------------------PRIVILEGS-------------------------

-- :name create-privilege! :! :1
-- :doc creates a new subscription
INSERT INTO privileges
(id, subscription, privilege, created)
VALUES (:id, :subscription, :privilege, :created)

-- :name get-sub-privileges :? :n
-- :doc list the privileges of a subscription by an user
SELECT privilege FROM privileges
WHERE subscription = :subscription_id

-- :name delete-privilege! :! :1
-- :doc revoke a privilege for a user on that subscription
DELETE privileges
WHERE subscription =
(SELECT id FROM subscriptions
WHERE subscribed_to =:group_id AND subscriber = :user_id)
