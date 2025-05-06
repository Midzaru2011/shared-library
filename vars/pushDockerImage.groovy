def call(String imageName, String imageTag, String dockerHubCreds) {
    echo "Pushing Docker image: ${imageName}:${imageTag}"
    docker.withRegistry('https://registry.hub.docker.com', dockerHubCreds) {
        docker.image("${imageName}:${imageTag}").push()
    }
}