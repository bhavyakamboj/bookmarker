
INSERT INTO users (email, password, name, role, created_at) VALUES
('admin@gmail.com', '$2a$10$ZuGgeoawgOg.6AM3QEGZ3O4QlBSWyRx3A70oIcBjYPpUB8mAZWY16', 'Admin', 'ROLE_ADMIN',CURRENT_TIMESTAMP()),
('siva@gmail.com', '$2a$10$CIXGKN9rPfV/mmBMYas.SemoT9mfVUUwUxueFpU3DcWhuNo5fexYC', 'Siva',  'ROLE_USER', CURRENT_TIMESTAMP())
;

INSERT INTO bookmarks(url, title, liked, archived, created_by,created_at) VALUES
('http://sivalabs.in','SivaLabs',true, false,1,CURRENT_TIMESTAMP()),
('http://dzone.com','DZone',false, false,1,CURRENT_TIMESTAMP()),
('http://javalobby.com','Javalobby',false, true,1,CURRENT_TIMESTAMP())
;
