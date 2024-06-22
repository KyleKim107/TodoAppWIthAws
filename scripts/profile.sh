#!/usr/bin/env bash

# Bash does not support return values, so the result is output using echo at the very end,
# and the client uses that value.

# Finding the idle profile: if real1 is in use, then real2 is idle, and vice versa.
function find_idle_profile()
{
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)
    # $(command) is “command substitution”.
    # ${parameter} is “parameter substitution”.

    # Checking if the Spring Boot application that Nginx is currently pointing to is running normally.
    # Generally, responses with status codes 400 or higher indicate an error.
    if [ ${RESPONSE_CODE} -ge 400 ] # If greater than 400 (including all 40x/50x errors)
    then
        CURRENT_PROFILE=real2
    else
        CURRENT_PROFILE=$(curl -s http://localhost/profile)
    fi
    echo "> Current profile - $CURRENT_PROFILE"
    if [ ${CURRENT_PROFILE} == real1 ]
    then
        IDLE_PROFILE=real2 # This profile is not connected to Nginx. It will be returned to connect with Spring.
    else
        IDLE_PROFILE=real1
    fi

    echo "${IDLE_PROFILE}"
    # Bash scripts do not have the ability to return values.
    # Therefore, the result is output using echo at the very end,
    # and the client captures that value to use in ($(find_idle_profile)).
    ### Do not use echo in the middle of the function.
}

# Finding the port of the idle profile
function find_idle_port()
{
    IDLE_PROFILE=$(find_idle_profile)

    if [ ${IDLE_PROFILE} == real1 ]
    then
        echo "8081"
    else
        echo "8082"
    fi
}
