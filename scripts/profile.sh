#!/usr/bin/env bash

# bash는 return value가 안되니 *제일 마지막줄에 echo로 해서 결과 출력*후, 클라이언트에서 값을 사용한다

# 쉬고 있는 profile 찾기: real1이 사용중이면 real2가 쉬고 있고, 반대면 real1이 쉬고 있음
function find_idle_profile()
{
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)
# $(command) is “command substitution”.
# ${parameter} is “parameter substitution”.

  # 현재 엔진엑스가 바라보고 있는 스트링 부트가 정상적으로 수행중인지 확인 보통 400이상의 응답은 오류
    if [ ${RESPONSE_CODE} -ge 400 ] # 400 보다 크면 (즉, 40x/50x 에러 모두 포함)
    then
        CURRENT_PROFILE=real2
    else
        CURRENT_PROFILE=$(curl -s http://localhost/profile)
    fi

    if [ ${CURRENT_PROFILE} == real1 ]
    then
      IDLE_PROFILE=real2 # 엔진엑스와 연결되지 않은 프로파일입니다. 이제 스프링과 연결하기 위해 반환합니다.
    else
      IDLE_PROFILE=real1
    fi

    echo "${IDLE_PROFILE}"
    # bash라는 스크립트는 값을 변환하는 기능이 없다
    #그래서 제일 마지막에 echo로 결과를 출력 후에 클라이언트에서 그 값을 잡아서
    # ($(find_idle_profile))에서 사용 합니다.
    ### 중간에 echo를 사용하지 말것
}

# 쉬고 있는 profile의 port 찾기
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