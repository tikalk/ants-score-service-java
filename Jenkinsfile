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
      	    sh '''
      	        cd src/main/serverless/functions/
      	        serverless deploy
      	    '''
      	}
    } catch (err) {
        currentBuild.result = 'FAILED'
        throw err
    }

}