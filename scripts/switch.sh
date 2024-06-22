#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
    IDLE_PORT=$(find_idle_port)

    echo "> Port to switch to: $IDLE_PORT"
    echo "> Switching port"
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

    # Overwrites the service_url file in /etc/nginx/conf.d/service-url.inc using sudo tee.
    #
    echo "> Reloading Nginx"
    sudo service nginx reload
}
