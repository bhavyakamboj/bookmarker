#!/bin/sh

declare project_dir=$(dirname $0)
declare docker_compose_file=${project_dir}/docker-compose.yml
declare docker_compose_platform_file=${project_dir}/docker-compose-platform.yml
declare spring_boot_bookmarker_api="spring-boot-bookmarker-api"
declare sonarqube="sonarqube"

function restart() {
    stop
    start
}

function start() {
    echo 'Starting bookmarker....'
    build_api
    docker-compose -f ${docker_compose_file} up --build --force-recreate -d ${spring_boot_bookmarker_api}
    docker-compose -f ${docker_compose_file} logs -f
}

function start_all() {
    echo 'Starting bookmarker and dependencies....'
    build_api
    docker-compose -f ${docker_compose_file} up --build --force-recreate -d
    docker-compose -f ${docker_compose_file} logs -f
}

function stop() {
    echo 'Stopping bookmarker....'
    docker-compose -f ${docker_compose_file} stop
    docker-compose -f ${docker_compose_file} rm -f
}

function build_api() {
    ./mvnw clean verify
}

function sonar() {
    echo 'Starting sonarqube....'
    docker-compose -f ${docker_compose_platform_file} up --build --force-recreate -d ${sonarqube}
    docker-compose -f ${docker_compose_platform_file} logs -f
}

action="start"

if [[ "$#" != "0"  ]]
then
    action=$@
fi

eval ${action}
