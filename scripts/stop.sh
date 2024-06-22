#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
# Allows using functions inside profile.sh
source ${ABSDIR}/profile.sh # Similar to import in Java

IDLE_PORT=$(find_idle_port)

echo "> Checking for the PID of the application running on port $IDLE_PORT"
IDLE_PID=$(lsof -t -i:${IDLE_PORT})

if [ -z ${IDLE_PID} ]
then
  echo "> No running application to stop."
else
  echo "> kill -15 $IDLE_PID"
  kill -15 ${IDLE_PID}
  sleep 5
fi
