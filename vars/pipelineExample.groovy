def call(body) {
    // Получаем параметры из переданного блока
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    // Вызываем функцию для настройки параметров
    def parameters = configureParameters(config)

    // Устанавливаем переменные окружения
    env.DOCKER_HUB_CREDENTIALS = credentials(parameters.dockerHubCredentials)
    env.IMAGE_NAME = parameters.imageName
    env.IMAGE_TAG = parameters.imageTag
    env.GITHUB_CREDENTIALS = parameters.githubCredentials

    node {
        try {
            stage('Delete workspace') {
                echo 'Deleting workspace'
                deleteDir()
            }

            stage('Checkout') {
                checkoutCode(
                    gitUrl: parameters.gitUrl,
                    gitBranch: parameters.gitBranch,
                    githubCredentials: env.GITHUB_CREDENTIALS
                )
            }

            stage('Prepare Dockerfile') {
                prepareDockerfile()
            }

            stage('Build Docker Image') {
                buildDockerImage(env.IMAGE_NAME, env.IMAGE_TAG)
            }

            stage('Push Docker Image') {
                pushDockerImage(env.IMAGE_NAME, env.IMAGE_TAG, env.DOCKER_HUB_CREDENTIALS)
            }
        } finally {
            stage('Cleanup') {
                echo 'Cleaning up workspace'
                cleanWs()
            }
        }
    }
}