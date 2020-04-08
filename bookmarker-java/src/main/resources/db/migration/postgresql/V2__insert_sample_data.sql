
INSERT INTO roles (id, name, created_at) VALUES
(1, 'ROLE_ADMIN', CURRENT_TIMESTAMP),
(2, 'ROLE_USER', CURRENT_TIMESTAMP)
;

INSERT INTO users (email, password, name, user_type, created_at) VALUES
('admin@gmail.com', '$2a$10$qW08MYCgzkYNoFFXYHUqluGGyLtBJK/XAQtu0lmsjD2mWaUiPQeZ2', 'Admin', 'LOCAL', CURRENT_TIMESTAMP),
('siva@gmail.com', '$2a$10$qW08MYCgzkYNoFFXYHUqluGGyLtBJK/XAQtu0lmsjD2mWaUiPQeZ2', 'Siva', 'LOCAL', CURRENT_TIMESTAMP),
('prasad@gmail.com', '$2a$10$qW08MYCgzkYNoFFXYHUqluGGyLtBJK/XAQtu0lmsjD2mWaUiPQeZ2', 'Prasad', 'LOCAL', CURRENT_TIMESTAMP)
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
('jpa'),
('hibernate'),
('junit'),
('devops'),
('maven'),
('gradle'),
('security')
;
