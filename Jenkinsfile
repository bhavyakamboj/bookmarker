#!groovy
@Library('jenkins-java-shared-library')
import com.sivalabs.JenkinsSharedLib

properties([
    parameters([
        booleanParam(defaultValue: false, name: 'PUBLISH_TO_DOCKERHUB', description: 'Publish Docker Image to DockerHub?'),
        booleanParam(defaultValue: false, name: 'DEPLOY_ON_HEROKU', description: 'Should deploy on Heroku?'),
        booleanParam(defaultValue: false, name: 'RUN_PERF_TESTS', description: 'Should run Performance Tests?')
    ])
])

env.DOCKER_USERNAME = 'sivaprasadreddy'
env.APPLICATION_NAME = 'bookmarker-kotlin'

def utils = new JenkinsSharedLib(this, env, params, scm, currentBuild)

node {

    try {
        utils.checkout()
        dir("bookmarker-kotlin") {
            utils.runMavenTests()
            utils.runOWASPChecks()
            utils.publishDockerImage()
        }
        dir("bookmarker-gatling-tests") {
            utils.runMavenGatlingTests()
        }
    }
    catch(err) {
        echo "ERROR: ${err}"
        currentBuild.result = currentBuild.result ?: "FAILURE"
    }
}