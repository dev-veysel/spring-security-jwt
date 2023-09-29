def checkout(){
    checkout([$class: 'GitSCM', branches: [[name: '*/master']],
              userRemoteConfigs: [[url: 'https://github.com/dev-veysel/spring-security-jwt.git']]])
}


def buildApp() {
    if (isUnix()) {
        sh './mvnw clean package'
    } else {
        bat 'mvnw clean package'
    }
}


def testApp() {
    if (isUnix()) {
        sh 'mvn test'
    } else {
        bat 'mvn test'
    }
}


def deployApp(){
    // Docker-Build
    bat 'docker build -t hackers-security .'

    // Docker-Push zu einem Docker-Registry (z. B. Docker Hub)
    // sh 'docker push DEIN_DOCKER_REGISTRY/DEIN_DOCKER_IMAGE_NAME:VERSION'

}

return this