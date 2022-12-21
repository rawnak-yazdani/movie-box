-- INSERT INTO genre (id, name) VALUES (nextval('genre_seq'), 'DRAMA');
INSERT INTO app_user (id, name, password, role, usercreationdate, username)
VALUES (nextval('user_seq'), 'General Akbur', '$2a$12$Ipl1UomhdKyXuvqxONgHduzspUycq6MWg9Ve9OfpUNlQAr4m71B9y', 'admin', now(), 'dictator')
ON CONFLICT (username) DO NOTHING ;