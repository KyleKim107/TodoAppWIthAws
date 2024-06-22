#!/bin/bash

REPOSITORY=/home/ec2-user/app/step3
PROJECT_NAME=TodoAppWithAws

echo "> Copying build file"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> Checking for the PID of the currently running application"

#CURRENT_PID=$(pgrep -fl ${PROJECT_NAME} | grep jar | awk '{print $1}')
CURRENT_PID=$(lsof -t -i:8080)

echo "PID of the currently running application: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> No running application, so no need to stop."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> Deploying new application"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> Adding execute permission to $JAR_NAME"

chmod +x $JAR_NAME

echo "> Running $JAR_NAME"

nohup java -jar \
    -Dspring.config.location=classpath:/application.properties,classpath:/application-real.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties \
    -Dspring.profiles.active=real \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
