#!/bin/bash

mvn clean install -DskipTests assembly:single -q
java -jar target/geektrust.jar /Users/ammar/Documents/dev/interview-tests/java-maven-starter-kit/sample_input/input1.txt