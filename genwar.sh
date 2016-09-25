#!/bin/bash

if [ $# -ne 1 ]; then
  echo -e "\nNeed Env as Arg\n"; exit 1
elif ! java -version 2>&1 | grep -q 1.7; then
  echo -e "\nJava Version is NOT 1.7\n"; exit 2
fi

SDIR=$(cd `dirname $0`; pwd -P)

if [ "$1" == "qa" ]; then
  cp job-server/src/main/resources/qa.conf job-server/src/main/resources/application.conf
fi

sbt package

if [ $? -ne 0 ]; then
  echo -e "\nsbt package failed\n"; exit 3
fi

WAR=`ls $SDIR/target/scala-2.1?/root_2.1?-*.war`

(cd job-server/src/main/resources; jar uf $WAR `ls`)

