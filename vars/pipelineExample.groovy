def call(body) {
    // Получаем параметры из переданного блока
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    // Задаем значения по умолчанию для параметров
    def dockerHubCreds = config.dockerHubCredentials ?: 'docker-hub-creds'
    def githubCreds = config.githubCredentials ?: 'github-credentials'
    def imageName = config.imageName ?: 'midzaru2011/coffeeandtea'
    def imageTag = config.imageTag ?: "v${env.BUILD_NUMBER}"
    def mavenTool = config.mavenTool ?: 'Maven-3.9.9'
    def gitUrl = config.gitUrl ?: 'https://github.com/Midzaru2011/CoffeeAndTea.git'
    def gitBranch = config.gitBranch ?: 'master'

    pipeline {
        agent any

        environment {
            DOCKER_HUB_CREDENTIALS = credentials(dockerHubCreds)
            IMAGE_NAME = imageName
            IMAGE_TAG = imageTag
            GITHUB_CREDENTIALS = githubCreds
        }

        tools {
            maven mavenTool
        }

        stages {
            stage('Delete workspace') {
                steps {
                    echo 'Deleting workspace'
                    deleteDir()
                }
            }

            stage('Checkout') {
                steps {
                    git branch: gitBranch,
                        credentialsId: env.GITHUB_CREDENTIALS,
                        url: gitUrl
                }
            }

            stage('Build Docker Image') {
                steps {
                    script {
                        docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                    }
                }
            }

            stage('Push Docker Image') {
                steps {
                    script {
                        docker.withRegistry('https://registry.hub.docker.com', dockerHubCreds) {
                            docker.image("${IMAGE_NAME}:${IMAGE_TAG}").push()
                        }
                    }
                }
            }
        }

        post {
            always {
                cleanWs() // Очищает workspace
            }
        }
    }
}