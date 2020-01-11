#!/bin/bash

#Yum Update
sudo yum -y update
#update java version 1.7 -> 1.8

sudo yum remove -y java*
sudo yum install -y java-1.8.0-openjdk-devel.x86_64
#change timezone
sudo rm /etc/localtime
sudo ln -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime
#install git
sudo yum install -y git
#install docker
sudo yum install -y docker
#start docker
sudo service docker start
sudo docker login
sudo docker pull redis:latest
sudo docker run -itd -p 6379:6379 --name redis redis:latest

#clone spring boot repository
mkdir ~/app && mkdir ~/app/step1
cd ~/app/step1
git clone https://github.com/seongpyoHong/Spring-boot-CRUD.git
REPOSITORY=/home/ec2-user/app/step1/Spring-boot-CRUD
cd $REPOSITORY

echo "> Git Pull"
git pull

echo "> 프로젝트 Build 시작"
./gradlew build --debug
echo "> Build 파일 복사"
cp ./build/libs/*.jar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -f springboot-webservice)

echo "$CURRENT_PID"

if [ -z $CURRENT_PID ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -2 $CURRENT_PID"
    kill -9 $CURRENT_PID
        sleep 5
fi

echo "> 새 어플리케이션 배포"
JAR_NAME=$(ls $REPOSITORY/ |grep 'jar' | tail -n 1)
chmod 744 /$REPOSITORY/$JAR_NAME
echo "> JAR Name: $JAR_NAME"
nohup java -jar \
-Dspring.config.location=/home/ec2-user/app/step1/Spring-boot-CRUD/src/main/resources/application.yaml,/home/ec2-user/app/application-oauth.yml,/home/ec2-user/app/application-real-db.yml \
$REPOSITORY/$JAR_NAME 2>&1 &