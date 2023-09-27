def checkout(){
    checkout([$class: 'GitSCM', branches: [[name: '*/master']],
              userRemoteConfigs: [[url: 'https://github.com/dev-veysel/spring-security-jwt.git']]])
}


def buildApp(){
    cmd './mvnw clean package'
}

def testApp(){
    cmd 'mvn test'
}

def deployApp(){
    // Docker-Build
    sh 'docker build -t SPRING_BOOT_ .'

    // Optional: Docker-Tagging für Versionskontrolle (ersetze VERSION durch die entsprechende Versionsnummer)
    sh 'docker tag DEIN_DOCKER_IMAGE_NAME:latest DEIN_DOCKER_REGISTRY/DEIN_DOCKER_IMAGE_NAME:VERSION'

    // Docker-Push zu einem Docker-Registry (z. B. Docker Hub)
    sh 'docker push DEIN_DOCKER_REGISTRY/DEIN_DOCKER_IMAGE_NAME:VERSION'

    // Optional: Bereinige nicht benötigte Docker-Images und Container
    sh 'docker system prune -f'
}

return this