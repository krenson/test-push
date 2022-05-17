echo "Running dispatcher locally (http://www.local.leforemhe.be/)"

rmdir /s /q dispatcher/out
cd dispatcher
bin\validator full -d out src
bin\docker_run.cmd out host.docker.internal:4503 80

read -p "Press enter to close the window"
