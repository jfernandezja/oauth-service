#!bin/bash

cp build/distributions/oauth-service.tar docker
docker build -t oauth-service docker
