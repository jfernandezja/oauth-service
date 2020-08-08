#!bin/bash

cp $WORKSPACE/build/distributions/oauth-service.tar $WORKSPACE/docker
docker build -t oauth-service docker/Dockerfile
