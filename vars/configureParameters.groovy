def call(Map config) {
    // Задаем значения по умолчанию для параметров
    def parameters = [
        dockerHubCredentials: config.dockerHubCredentials ?: 'docker-hub-creds',
        githubCredentials: config.githubCredentials ?: 'github-credentials',
        imageName: config.imageName ?: 'midzaru2011/coffeeandtea',
        imageTag: config.imageTag ?: "v${env.BUILD_NUMBER}",
        gitUrl: config.gitUrl ?: 'https://github.com/Midzaru2011/CoffeeAndTea.git',
        gitBranch: config.gitBranch ?: 'master'
    ]

    echo "Configuration loaded: ${parameters}"
    return parameters
}