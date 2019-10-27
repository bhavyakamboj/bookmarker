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
            utils.runMavenTests("Kotlin Tests")
            utils.runOWASPChecks("Kotlin OWASP")
            utils.publishDockerImage("Kotlin PublishDocker", DOCKER_USERNAME, API_IMAGE_NAME_KOTLIN)
            utils.deployOnHeroku("Kotlin Heroku Deployment")
        }
        dir("bookmarker-java") {
            utils.runMavenTests("Java Tests")
            utils.runOWASPChecks("Java OWASP")
            utils.publishDockerImage("Java PublishDocker", DOCKER_USERNAME, API_IMAGE_NAME_JAVA)
            utils.deployOnHeroku("Java Heroku Deployment")
        }
        dir("bookmarker-ui-vue") {
            utils.npmBuild("UI-Vue Build")
            utils.npmTest("UI-Vue Test")
            utils.publishDockerImage("UI-Vue PublishDocker", DOCKER_USERNAME, UI_IMAGE_NAME_VUE)
        }
        dir("bookmarker-gatling-tests") {
            utils.runMavenGatlingTests("Perf Test")
        }
    }
    catch(err) {
        echo "ERROR: ${err}"
        currentBuild.result = currentBuild.result ?: "FAILURE"
    }
}