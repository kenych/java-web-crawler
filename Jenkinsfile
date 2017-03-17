
pipeline {
    agent any

    environment {
        PROJECT = 'web_crawler'
    }

    stages {
        stage('get') {
            steps {
                sh '''
                rm -rf $PROJECT
                ls
                git clone "https://kenych@bitbucket.org/kenych/$PROJECT.git"
                cd $PROJECT

                ls
                '''
            }
        }

        stage('build') {
            steps {
                sh '''
                pwd
                cd $PROJECT
                ls

                mvn install
                '''

            }

        }


        stage('dockerise') {
            steps {
                sh '''
                pwd
                cd $PROJECT/src/main/container/
                ls
                cp ../../../target/crawler-1.0-SNAPSHOT.jar files
                docker build  -t web-crawler:current .
                docker run  -t web-crawler:current  http://www.example.com http://www.example.com 5
                '''
            }
        }


    }

}
