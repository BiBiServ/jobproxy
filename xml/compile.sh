#!/bin/bash

TARGET=../src/main/java
PACKAGE=de.unibi.cebitec.bibiserv.jobproxy.data


xjc -d $TARGET -p $PACKAGE.task JobTask.xsd
xjc -d $TARGET -p $PACKAGE.state JobState.xsd
