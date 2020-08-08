#!bin/bash

cp ../build/distributions/oauth-service.tar .
docker build -t oauth-service docker/Dockerfile
