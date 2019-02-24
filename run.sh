#!/bin/sh

declare project_dir=$(dirname $0)
declare docker_compose_file=${project_dir}/docker-compose.yml
declare docker_compose_platform_file=${project_dir}/etc/docker-compose-platform.yml
declare backend="backend"
declare sonarqube="sonarqube"

function restart() {
    stop
    start
}

function start() {
    echo 'Starting bookmarker....'
    build_api
    docker-compose -f ${docker_compose_file} up --build --force-recreate -d ${backend}
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
