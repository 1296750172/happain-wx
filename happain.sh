#!/bin/bash
pwd
kill -9 $(lsof -i:80 -t)
/usr/local/maven3.8.5/bin/mvn clean
/usr/local/maven3.8.5/bin/mvn package
nohup java -jar ./target/wx-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
