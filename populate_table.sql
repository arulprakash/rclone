COPY users( id,first_name,last_name,pass,email,created,changed,admin,is_active ) FROM 'C:\Users\Amali\Documents\DBDump\Users.csv' DELIMITER ',' CSV HEADER;
COPY groups( id,description, rules, created,changed, created_by) FROM 'C:\Users\Amali\Documents\DBDump\Groups.csv' DELIMITER ',' CSV HEADER;
COPY subscriptions( id,subscribed_to, subscriber, created) FROM 'C:\Users\Amali\Documents\DBDump\Subscriptions.csv' DELIMITER ',' CSV HEADER;
COPY posts( id,title, url, description, votes, created, changed, posted_by, posted_in) FROM 'C:\Users\Amali\Documents\DBDump\Posts.csv' DELIMITER ',' CSV HEADER;
COPY COMMENTS( id, description, votes, commented_by, posted_to, replied_to, created, changed) FROM 'C:\Users\Amali\Documents\DBDump\Comments.csv' DELIMITER ',' CSV HEADER;