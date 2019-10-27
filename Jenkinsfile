#!groovy
@Library('jenkins-shared-library')
import com.sivalabs.JenkinsSharedLib

properties([
    parameters([
        booleanParam(defaultValue: false, name: 'PUBLISH_TO_DOCKERHUB', description: 'Publish Docker Image to DockerHub?'),
        booleanParam(defaultValue: false, name: 'DEPLOY_ON_HEROKU', description: 'Should deploy on Heroku?'),
        booleanParam(defaultValue: false, name: 'RUN_PERF_TESTS', description: 'Should run Performance Tests?')
    ])
])

def DOCKER_USERNAME = 'sivaprasadreddy'
def API_IMAGE_NAME_KOTLIN = 'bookmarker-kotlin'
def API_IMAGE_NAME_JAVA = 'bookmarker-java'
def UI_IMAGE_NAME_VUE = 'bookmarker-ui-vue'

def utils = new JenkinsSharedLib(this, env, params, scm, currentBuild)

node {

    try {
        utils.checkout()
        dir("bookmarker-kotlin") {
            utils.runMavenTests()
            utils.runOWASPChecks()
            utils.publishDockerImage(DOCKER_USERNAME, API_IMAGE_NAME_KOTLIN)
            utils.deployOnHeroku()
        }
        dir("bookmarker-java") {
            utils.runMavenTests()
            utils.runOWASPChecks()
            utils.publishDockerImage(DOCKER_USERNAME, API_IMAGE_NAME_JAVA)
            utils.deployOnHeroku()
        }
        dir("bookmarker-ui-vue") {
            utils.npmBuild()
            utils.npmTest()
            utils.publishDockerImage(DOCKER_USERNAME, UI_IMAGE_NAME_VUE)
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