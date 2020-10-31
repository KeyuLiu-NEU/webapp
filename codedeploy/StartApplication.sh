#!/bin/bash
cd /home/ubuntu
setsid java -jar CloudNativeWebApp-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 &