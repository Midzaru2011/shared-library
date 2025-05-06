def call(Map config) {
    def gitUrl = config.gitUrl ?: 'https://github.com/Midzaru2011/CoffeeAndTea.git'
    def gitBranch = config.gitBranch ?: 'master'
    def githubCreds = config.githubCredentials ?: 'github-credentials'

    echo "Checking out code from ${gitUrl} (branch: ${gitBranch})"
    git branch: gitBranch,
        credentialsId: githubCreds,
        url: gitUrl
}