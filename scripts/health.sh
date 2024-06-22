#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh
source ${ABSDIR}/switch.sh

IDLE_PORT=$(find_idle_port)

echo "> Health Check Start!"
echo "> IDLE_PORT: $IDLE_PORT"
echo "> curl -s http://localhost:$IDLE_PORT/profile "
sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -s http://localhost:${IDLE_PORT}/profile)
  UP_COUNT=$(echo ${RESPONSE} | grep 'real' | wc -l)
  echo "UP_COUNT = $UP_COUNT"

  if [ ${UP_COUNT} -ge 1 ]
  then # $up_count >= 1 (Verifies if the "real" string is present)
      echo "> Health check successful"
      switch_proxy
      break
  else
      echo "> Cannot determine the response of the health check or it is not running."
      echo "> Health check: ${RESPONSE}"
  fi

  if [ ${RETRY_COUNT} -eq 10 ]
  then
    echo "> Health check failed."
    echo "> Stopping deployment without connecting to Nginx."
    exit 1
  fi

  echo "> Health check connection failed. Retrying..."
  sleep 10
done
