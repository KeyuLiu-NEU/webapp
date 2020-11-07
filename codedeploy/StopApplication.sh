#!/bin/bash
cd /home/ubuntu
sudo fuser -k 8080/tcp
if [  -f "CloudNativeWebApp-0.0.1-SNAPSHOT.jar" ];then
rm CloudNativeWebApp-0.0.1-SNAPSHOT.jar
fi