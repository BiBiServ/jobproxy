#!/bin/bash

#clean vagrant
vagrant suspend
vagrant destroy -f
vagrant box remove -f "ubuntu/xenial64"

#build the project
cd ..
mvn clean compile
mvn package
cd -

#start vagrant
vagrant up

#First test: ping
PING_RESPONSE=$(curl 127.0.0.1:9999/v1/jobproxy/ping)
if [[ $PING_RESPONSE == *"alive"* ]]; then
  echo "[PING TEST] passed"
else
  echo "[PING TEST] failed"
  vagrant destroy -f
  exit
fi

#second test submitting a job
curl -H "Content-Type: application/json" -X POST -d @task_test.json http://127.0.0.1:9999/v1/jobproxy/submit

#third test running a job in docker container

