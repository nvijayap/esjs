#!/bin/bash

if java -version 2>&1 | grep -q 1.7 ; then
  :
else
  echo "=> Please use Java 1.7" ; exit 1
fi

sbt clean assembly

