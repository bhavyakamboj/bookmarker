# Bookmarker

Bookmarker is a simple bookmarking application developed using Kotlin, SpringBoot, VueJS.

[![Build Status](https://travis-ci.org/sivaprasadreddy/bookmarker.svg?branch=master)](https://travis-ci.org/sivaprasadreddy/bookmarker)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.sivalabs%3Abookmarker&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.sivalabs%3Abookmarker)

## Backend Tech Stack
* Java8 / Kotlin
* SpringBoot
* H2(Dev) / Postgres (Prod)
* Spring Data JPA
* Spring Security JWT Authentication
* Maven

## Frontend Tech Stack
* VueJS 2.5
* Vuex, Vue-router
* Bootstrap 4

## Run tests

`> ./mvnw clean verify`

## Run application locally

`> ./mvnw clean package & java -jar backend/target/bookmarker-0.0.1.jar`

## Running using Docker

To start application and Postgres

`> ./run.sh start`

To start application and all dependent services like ELK, grafana, prometheus

`> ./run.sh start_all`

* Application: http://localhost:8080/
* SwaggerUI: http://localhost:8080/swagger-ui.html
* Prometheus: http://localhost:9090/
* Grafana: http://localhost:3000/ (admin/admin)
* Kibana: http://localhost:5601/ 

### Run Performance Tests

`performance-tests> ./gradlew gatlingRun`

### Run SonarQube analysis

```
> ./run.sh sonar
> ./mvnw clean verify -P sonar -Dsonar.login=$SONAR_LOGIN_TOKEN
```

## TODO

* User forgot password, reset password
* Bookmark like feature
* Weekly Email NewsLetter
* Improve Test coverage and readability of tests
* Use Vuetify for UI
* Refactor Gatling tests to simulate typical user behaviour
* Script to automatically create Grafana dashboard
* Add gradle based build


## Contributing
If you want to contribute to add new feature or improve existing code quality please raise issues and ofcourse, Pull Requests are welcome.

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
