pipeline {
    agent any

     environment {
            DOCKER_IMAGE = "navishkadarshana/spring-boot-app:latest"
            DOCKER_CREDENTIALS_ID = "dockerhub-credentials"
            SSH_CREDENTIALS_ID = "learnfi-prod-server"
            SSH_TARGET = "ubuntu@54.254.18.85"
            DOCKER_CONTAINER = "spring-boot-app"
     }

    tools {
        maven 'Maven 3.9.7'
    }


    stages {

        stage('Checkout') {
            steps {
                git branch: 'dev', url: 'https://gitlab.com/jevigsoft/my-cicd-project.git', credentialsId: 'jevigsoft-gitlab-credentials'
            }
        }

        stage('Build') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'learnfi-prod-database-credentials', usernameVariable: 'DB_USERNAME', passwordVariable: 'DB_PASSWORD')]) {
                        sh '''#!/bin/bash
                        echo "Building application with MySQL database"
                        export DB_USERNAME=$DB_USERNAME
                        export DB_PASSWORD=$DB_PASSWORD
                        mvn clean package
                        '''
                    }
                }
            }
        }



        stage('Test') {
            steps {
                script {
                     withCredentials([usernamePassword(credentialsId: 'learnfi-prod-database-credentials', usernameVariable: 'DB_USERNAME', passwordVariable: 'DB_PASSWORD')]) {
                        sh '''#!/bin/bash
                        echo "Testing application with MySQL database"
                        export DB_USERNAME=$DB_USERNAME
                        export DB_PASSWORD=$DB_PASSWORD
                        mvn test
                        '''
                    }
                }
            }
        }


        stage('Docker Build & Push') {
            steps {
                script {
                      withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                           sh '''#!/bin/bash
                           docker login -u $DOCKER_USER -p $DOCKER_PASS
                           docker build -t $DOCKER_IMAGE .
                           docker push $DOCKER_IMAGE
                           docker logout
                           '''
                      }
                }
            }
        }


        stage('Deploy') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'learnfi-prod-database-credentials', usernameVariable: 'DB_USERNAME', passwordVariable: 'DB_PASSWORD')]) {
                        sshagent(['learnfi-prod-server']) {
                                sh '''
                                ssh -o StrictHostKeyChecking=no $SSH_TARGET << EOF
                                docker login -u $DOCKER_USER -p $DOCKER_PASS
                                docker pull $DOCKER_IMAGE
                                docker stop $DOCKER_CONTAINER || true
                                docker rm $DOCKER_CONTAINER || true
                                docker run -d --name $DOCKER_CONTAINER -p 8080:8080 \
                                -e DB_USERNAME=$DB_USERNAME \
                                -e DB_PASSWORD=$DB_PASSWORD \
                                $DOCKER_IMAGE
                                docker logout
                                '''
                        }
                    }
                }
            }
        }

    }


    post {
        always {
            cleanWs()
        }

        success {
            emailext (
                            to: 'jevigsoft@gmail.com',
                            subject: "SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                            body: """<p>Good news! The job <b>${env.JOB_NAME}</b> build <b>${env.BUILD_NUMBER}</b> succeeded.</p>""",
                            replyTo: 'noreply@learnfi.lk',
                            from: 'noreply@learnfi.lk'
                      )
        }


        failure {
            emailext (
                            to: 'jevigsoft@gmail.com',
                            subject: "FAILURE: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                            body: """<p>Unfortunately, the job <b>${env.JOB_NAME}</b> build <b>${env.BUILD_NUMBER}</b> failed.</p>""",
                            replyTo: 'noreply@learnfi.lk',
                            from: 'noreply@learnfi.lk'
                      )
        }

    }


}