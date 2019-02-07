
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
('jpa'),
('hibernate'),
('junit'),
('devops'),
('maven'),
('gradle'),
('security'),
;

INSERT INTO bookmarks(url, title, created_by,created_at) VALUES
('http://sivalabs.in','SivaLabs',1,CURRENT_TIMESTAMP()),
('http://dzone.com','DZone',1,CURRENT_TIMESTAMP()),
('https://vladmihalcea.com/','VladMihalcea Blog',1,CURRENT_TIMESTAMP()),
('https://linuxize.com/post/how-to-remove-docker-images-containers-volumes-and-networks/','How To Remove Docker Containers, Images, Volumes, and Networks',1,CURRENT_TIMESTAMP()),
('https://reflectoring.io/unit-testing-spring-boot/','All You Need To Know About Unit Testing with Spring Boot',1,CURRENT_TIMESTAMP()),
('https://winterbe.com/posts/2018/08/29/migrate-maven-projects-to-java-11-jigsaw/','Migrate Maven Projects to Java 11',1,CURRENT_TIMESTAMP()),
('https://www.devglan.com/spring-security/spring-boot-jwt-auth','Spring Boot Security Jwt Authentication',1,CURRENT_TIMESTAMP()),
('https://junit.org/junit5/docs/current/user-guide/','JUnit 5 User Guide',1,CURRENT_TIMESTAMP()),
('https://shekhargulati.com/','Shekhar Gulati Blog',1,CURRENT_TIMESTAMP()),
('https://shekhargulati.com/2019/01/13/running-tests-and-building-react-applications-with-gradle-build-tool/','Running Tests and Building React applications with Gradle Build Tool',1,CURRENT_TIMESTAMP()),
;

INSERT INTO bookmark_tag(bookmark_id, tag_id) VALUES
(1,1),
(1,2),
(2,1),
(2,3),
(3,1),
(3,4)
;
