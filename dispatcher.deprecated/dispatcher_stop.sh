#!/bin/sh

rm -rf out
rm -rf dispatcher.log
docker ps
docker stop $(docker ps -aq)
docker ps