#!/bin/sh
sudo docker stop edgeconvert
sudo docker rm $(sudo docker ps -a -q) 
sudo docker image rm $(sudo docker images -a -q)
