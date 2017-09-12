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

-- :name create-comments! :! :n
-- :doc creates a new post
INSERT INTO comments
(id, title, url, description, created, posted_by, posted_in)
VALUES (:id, :title, :url, :description, :created, :posted_by, :posted_in)

-- :name update-posts! :! :n
-- :doc update an existing group's description or rules
UPDATE posts
SET description = :description, rules = :rules, changed = :changed
WHERE id = :id

-- :name get-user-comments :? :n
-- :doc retrieve all the posts posted by the user
SELECT * FROM posts
WHERE posted_by = :id

-- :name delete-group! :! :n
-- :doc delete a user given the id
DELETE FROM posts
WHERE id = :id