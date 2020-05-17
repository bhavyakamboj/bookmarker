#!groovy
@Library('jenkins-shared-library')
import com.sivalabs.JenkinsSharedLib

properties([
    parameters([
        booleanParam(defaultValue: false, name: 'PUBLISH_TO_DOCKERHUB', description: 'Publish Docker Image to DockerHub?'),
        booleanParam(defaultValue: false, name: 'DEPLOY_ON_HEROKU', description: 'Should deploy on Heroku?')
    ])
])

def DOCKER_USERNAME = 'sivaprasadreddy'
def API_IMAGE_NAME = 'bookmarker'

def utils = new JenkinsSharedLib(this, env, params, scm, currentBuild)

node {

    try {
        utils.checkout()

        utils.runMavenTests("Test")
        utils.runOWASPChecks("OWASP Checks")
        utils.publishDockerImage("Publish Docker Image", DOCKER_USERNAME, API_IMAGE_NAME)
        utils.deployOnHeroku("Heroku Deployment")

    }
    catch(err) {
        echo "ERROR: ${err}"
        currentBuild.result = currentBuild.result ?: "FAILURE"
    }
}
