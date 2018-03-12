properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')), disableConcurrentBuilds()])

node {
    def workspace = pwd()
    def zipFile = "${workspace}/build/distributions/ants-score-service-1.0.zip"
    try {
        stage ('Clone') {
        	checkout scm
        }
        stage ('Build') {
        	sh './gradlew clean build'
        }
      	stage ('Deploy') {
      	    sh "npm init -y"
            sh "npm install serverless-domain-manager --save-dev"
            sh "npm install serverless-plugin-tracing --save-dev"
            sh "serverless deploy"
      	}
    } catch (err) {
        currentBuild.result = 'FAILED'
        throw err
    }

}