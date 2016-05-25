#!/bin/sh

export JAVA_HOME='/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home'
export MAVEN_OPTS="-Xmx4G -Xms4G -XX:PermSize=128m -XX:MaxPermSize=128m"

while [ true ]
do
    mvn -DskipTests spring-boot:run
done