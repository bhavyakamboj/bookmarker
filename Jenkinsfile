pipeline {
    agent any

    triggers {
            pollSCM('* * * * *')
    }

    environment {
        DOCKER_USERNAME = 'sivaprasadreddy'
        APPLICATION_NAME = 'bookmarker'
        BACKEND_MODULE = 'backend'
    }

    parameters {
        booleanParam(name: 'PUBLISH_TO_DOCKERHUB', defaultValue: false, description: 'Publish Docker Image to DockerHub?')
    }

    stages {
        stage('Test') {
            steps {
                sh './mvnw clean verify'
            }
            post {
                always {
                    dir(BACKEND_MODULE) {
                        junit 'target/surefire-reports/*.xml'
                        junit 'target/failsafe-reports/*.xml'
                    }
                }
            }
        }

        stage('OWASP Dependency Check') {
            steps {
                dir(BACKEND_MODULE) {
                    sh '../mvnw dependency-check:check'
                }
            }
            post {
                always {
                    dir(BACKEND_MODULE) {
                        publishHTML(target:[
                             allowMissing: true,
                             alwaysLinkToLastBuild: true,
                             keepAll: true,
                             reportDir: 'target',
                             reportFiles: 'dependency-check-report.html',
                             reportName: "OWASP Dependency Check Report"
                        ])
                    }
                }
            }
        }

        stage("Publish to DockerHub") {
            when {
                expression { params.PUBLISH_TO_DOCKERHUB == true }
            }
            steps {
                dir(BACKEND_MODULE) {
                      sh "docker build -t ${env.DOCKER_USERNAME}/${env.APPLICATION_NAME}:${BUILD_NUMBER} -t ${env.DOCKER_USERNAME}/${env.APPLICATION_NAME}:latest ."

                      withCredentials([[$class: 'UsernamePasswordMultiBinding',
                                        credentialsId: 'docker-hub-credentials',
                                        usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                          sh "docker login --username $USERNAME --password $PASSWORD"
                      }
                      sh "docker push ${env.DOCKER_USERNAME}/${env.APPLICATION_NAME}:latest"
                      sh "docker push ${env.DOCKER_USERNAME}/${env.APPLICATION_NAME}:${BUILD_NUMBER}"
                }
            }
        }
    }
}
