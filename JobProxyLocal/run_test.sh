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
