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
            utils.runMavenTests("BookMarker-Kotlin Tests")
            utils.runOWASPChecks("BookMarker-Kotlin OWASP")
            utils.publishDockerImage("BookMarker-Kotlin PublishDocker", DOCKER_USERNAME, API_IMAGE_NAME_KOTLIN)
            utils.deployOnHeroku("BookMarker-Kotlin Heroku Deployment")
        }
        dir("bookmarker-java") {
            utils.runMavenTests("BookMarker-Java Tests")
            utils.runOWASPChecks("BookMarker-Java OWASP")
            utils.publishDockerImage("BookMarker-Java PublishDocker", DOCKER_USERNAME, API_IMAGE_NAME_JAVA)
            utils.deployOnHeroku("BookMarker-Java Heroku Deployment")
        }
        dir("bookmarker-ui-vue") {
            utils.npmBuild("BookMarker-UI-Vue Build")
            utils.npmTest("BookMarker-UI-Vue Test")
            utils.publishDockerImage("BookMarker-UI-Vue PublishDocker", DOCKER_USERNAME, UI_IMAGE_NAME_VUE)
        }
        dir("bookmarker-gatling-tests") {
            utils.runMavenGatlingTests("BookMarker Perf Test")
        }
    }
    catch(err) {
        echo "ERROR: ${err}"
        currentBuild.result = currentBuild.result ?: "FAILURE"
    }
}