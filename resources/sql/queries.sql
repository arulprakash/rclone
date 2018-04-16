-- :name create-user! :! :1
-- :doc creates a new user record
INSERT INTO users
(id, first_name, last_name, email, pass, created, is_active)
VALUES (:id, :first_name, :last_name, :email, :pass, :created, :is_active)

-- :name update-user! :! :1
-- :doc update an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email, changed = :changed
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieve a user given the id.
SELECT * FROM users
WHERE id = :id AND pass = :pass

-- :name delete-user! :! :1
-- :doc delete a user given the id
DELETE FROM users
WHERE id = :id

--------------------------GROUPS-------------------------

-- :name create-group! :! :1
-- :doc creates a new group
INSERT INTO groups
(id, description, created, created_by)
VALUES (:id, :description, :created, :created_by)

-- :name update-group! :! :1
-- :doc update an existing group's description or rules
UPDATE groups
SET description = :description, rules = :rules, changed = :changed
WHERE id = :id

-- :name get-groups :? :*
-- :doc retrieve a user given the id.
SELECT * FROM groups
WHERE id = :id

-- :name delete-group! :! :1
-- :doc delete a user given the id
DELETE FROM groups
WHERE id = :id

--------------------------POSTS-------------------------

-- :name create-post! :<!
-- :doc creates a new post
INSERT INTO posts
(title, url, description, created, posted_by, posted_in)
VALUES (:title, :url, :description, :created, :posted_by, :posted_in)
RETURNING id;

-- :name create-temp! :<!
-- :doc creates a new post
INSERT INTO posts :i*:cols
VALUES (:v*:ids)
RETURNING id;


-- :name upvote-post! :! :1
-- :doc upvote an existing post
UPDATE posts
SET votes = votes + 1
WHERE id = :id

-- :name downvote-post! :! :1
-- :doc downvote an existing post
UPDATE posts
SET votes = votes - 1
WHERE id = :id

-- :name get-user-posts :? :*
-- :doc retrieve all the posts posted by the user
SELECT * FROM posts
WHERE posted_by = :id

-- :name get-top-posts :? :*
-- :doc retrieve all the posts posted by the user
SELECT * FROM posts
ORDER BY votes
LIMIT 50

-- :name delete-post! :! :1
-- :doc delete a user given the id
DELETE FROM posts
WHERE id = :id
--------------------------COMMENTS-------------------------

-- :name create-comment! :>!
-- :doc creates a new post
INSERT INTO comments
(description, votes, created, posted_to, replied_to, commented_by)
VALUES (:description, :votes, :created, :posted_to, :replied_to, :commented_by)
RETURNING id

-- :name update-comment! :! :1
-- :doc update an existing group's description or rules
UPDATE comments
SET description = :description
WHERE id = :id

-- :name get-user-comments :? :*
-- :doc retrieve all the posts posted by the user
SELECT * FROM comments
WHERE posted_by = :id

-- :name get-post-comments :? :*
-- :doc retrieve all the posts posted by the user
SELECT * FROM comments
WHERE posted_in = :id

-- :name delete-comment! :! :1
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

-- :name get-user-subs :? :*
-- :doc retrieve all the subscriptions by an user
SELECT subscribed_to FROM subscriptions
WHERE subscriber = :id

-- :name delete-sub! :! :1
-- :doc delete a sub given the id
DELETE subscriptions
WHERE subscribed_to =:group_id AND subscriber = :user_id

--------------------------PRIVILEGES-------------------------

-- :name create-privilege! :! :1
-- :doc creates a new subscription
INSERT INTO privileges
(id, subscription, privilege, created)
VALUES (:id, :subscription, :privilege, :created)

-- :name get-sub-privileges :? :*
-- :doc list the privileges of a subscription by an user
SELECT privilege FROM privileges
WHERE subscription = :subscription_id

-- :name delete-privilege! :! :1
-- :doc revoke a privilege for a user on that subscription
DELETE privileges
WHERE subscription =
(SELECT id FROM subscriptions
WHERE subscribed_to =:group_id AND subscriber = :user_id)
