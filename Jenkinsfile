pipeline {
    agent any
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Deliver') {
            steps {
                sh 'java -cp target/classes App 6 0.2 15 0.45 4 0.495'
                archiveArtifacts artifacts: 'resultados.txt, target/*.jar', fingerprint: true
            }
        }
    }
}