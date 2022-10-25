#!/bin/sh

bin/validator full -d out src
bin/docker_run.cmd out localhost:4503 test
bin/docker_run.cmd out host.docker.internal:4503 80 >dispatcher.log 2>&1 &

tail -f dispatcher.log