
INSERT INTO roles (id, name, created_at) VALUES
(1, 'ROLE_ADMIN', CURRENT_TIMESTAMP()),
(2, 'ROLE_USER', CURRENT_TIMESTAMP())
;

INSERT INTO users (email, password, name, created_at) VALUES
('admin@gmail.com', '$2a$10$ZuGgeoawgOg.6AM3QEGZ3O4QlBSWyRx3A70oIcBjYPpUB8mAZWY16', 'Admin', CURRENT_TIMESTAMP()),
('siva@gmail.com', '$2a$10$CIXGKN9rPfV/mmBMYas.SemoT9mfVUUwUxueFpU3DcWhuNo5fexYC', 'Siva',  CURRENT_TIMESTAMP()),
('prasad@gmail.com', '$2a$10$vtnCx8LxraSbveB26Lth3.s/.9hI1SFHwCFTSlAkAlVRybva6GQo6', 'Prasad',  CURRENT_TIMESTAMP())
;

INSERT INTO user_role (user_id, role_id) VALUES
(1, 1),
(1, 2),
(2, 2),
(3, 2)
;

INSERT INTO tags(name) VALUES
('java'),
('spring'),
('spring-boot'),
('spring-cloud'),
('jpa')
;

INSERT INTO bookmarks(url, title, created_by,created_at) VALUES
('http://sivalabs.in','SivaLabs',1,CURRENT_TIMESTAMP()),
('http://dzone.com','DZone',1,CURRENT_TIMESTAMP()),
('http://javalobby.com','Javalobby',1,CURRENT_TIMESTAMP())
;

INSERT INTO bookmark_tag(bookmark_id, tag_id) VALUES
(1,1),
(1,2),
(2,1),
(2,3),
(2,4),
(3,3)
;
