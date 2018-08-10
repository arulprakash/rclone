--COPY users( id,first_name,last_name,pass,email,created,changed,admin,is_active ) FROM '/Users/arul/Downloads/Users.csv' DELIMITER ',' CSV HEADER;

--COPY groups( id,description, rules, created,changed, created_by) FROM '/Users/arul/Downloads/Groups.csv' DELIMITER ',' CSV HEADER;

--COPY subscriptions( id,subscribed_to, subscriber, created) FROM '/Users/arul/Downloads/Subscriptions.csv' DELIMITER ',' CSV HEADER;

--COPY posts( id,title, url, description, votes, created, changed, posted_by, posted_in) FROM '/Users/arul/Downloads/Posts.csv' DELIMITER ',' CSV HEADER;


COPY COMMENTS( id, description, votes, commented_by, posted_to, replied_to, created, changed) FROM '/Users/arul/Downloads/Comments.csv' DELIMITER ',' CSV HEADER;