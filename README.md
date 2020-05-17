# Bookmarker

Bookmarker is a simple bookmarking application developed using SpringBoot.

## Motivation
The motivation behind building this simple application is try out stuff before using in production apps.
There are plenty of new tools, libraries and frameworks coming in everyday. 
I personally don't like using new tools without playing around with them for a while 
because "Everything just works great in HELLO WORLD apps" :wink: but is hard to use in real projects.

Also, occasionally we want to quickly try out something to see if it is working or not.
Trying them in our real application might not be easy because of various reasons.
So, in those situations having a simple but not as simple as Hello World app comes handy.
Hence this **Bookmarker** application :-)


![Bookmarker Master CI](https://github.com/sivaprasadreddy/bookmarker/workflows/Bookmarker%20Master%20CI/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.sivalabs%3Abookmarker-java&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.sivalabs%3Abookmarker-java)

#### Live Demo 
https://sivalabs-bookmarker.herokuapp.com/

## Tech Stack
* Java
* SpringBoot 2.x
* H2(Dev) / Postgres (Prod)
* Spring Data JPA
* Spring Security Authentication
* Jasypt
* Swagger2
* Flyway
* SonarQube
* Jacoco
* Maven
* JUnit 5, Mockito, Testcontainers

## How to run?

### Run tests

`bookmarker> ./mvnw clean verify`

### Run application locally

`bookmarker> ./mvnw clean package & java -jar target/bookmarker-0.0.1-SNAPSHOT.jar`

### Running using Docker

To start application and Postgres

`bookmarker> ./run.sh start`

To start application and all dependent services like ELK, grafana, prometheus

`bookmarker> ./run.sh start_all`

* Application: http://localhost:8080/
* SwaggerUI: http://localhost:8080/swagger-ui.html
* Prometheus: http://localhost:9090/
* Grafana: http://localhost:3000/ (admin/admin)
* Kibana: http://localhost:5601/ 

### Encrypt or decrypt secrets

Encrypt properties: `./mvnw jasypt:encrypt-value -Djasypt.encryptor.password="pwd" -Djasypt.plugin.value="plain-text"`

Decrypt properties: `./mvnw jasypt:decrypt-value -Djasypt.encryptor.password="pwd" -Djasypt.plugin.value="encrypted-text"`


### Run Performance Tests

`bookmarker> ./mvnw gatling:test`

### Run SonarQube analysis

```
bookmarker> ./run.sh sonar
bookmarker> ./mvnw clean verify -P sonar -Dsonar.login=$SONAR_LOGIN_TOKEN
```

## TODO

* Social Login with Google, GitHub
* User forgot password, reset password
* Bookmark like feature
* Weekly Email NewsLetter
* Improve Test coverage
* Refactor Gatling tests to simulate typical user behaviour
* ~~Script to automatically create Grafana dashboard~~ :white_check_mark:
* Deploy on Heroku


## Contributing
If you want to contribute to add new feature or improve existing code quality 
please raise issues and of course, Pull Requests are welcome.

## References

* https://spring.io/projects/spring-boot
* https://start.spring.io/
* https://prometheus.io/
* https://grafana.com/
* https://docs.docker.com/
* https://jenkins.io/doc/
* https://www.elastic.co/elk-stack
* https://gatling.io/
* https://www.sonarqube.org/
* https://sonarcloud.io/
