def call(String imageName, String imageTag) {
    echo "Building Docker image: ${imageName}:${imageTag}"
    docker.build("${imageName}:${imageTag}")
}