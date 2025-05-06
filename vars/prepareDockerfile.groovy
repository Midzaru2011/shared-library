def call() {
    echo 'Preparing Dockerfile from shared library'
    writeFile file: 'Dockerfile', text: libraryResource('Dockerfile')
}