#!/bin/bash
export SOURCES=src
export CLASSES=classes
export CLASSPATH=`lib/ap.jar`

javac -cp ${CLASSPATH} -sourcepath ${SOURCES} -d ${CLASSES} $@ `find src -name "*.java"`
cp ressources/* classes/
