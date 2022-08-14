#!/bin/bash
echo "Subindo Aplicação"
mvn clean install
sudo service docker start
docker compose -f "$PWD"/run/docker-compose.yaml up --build

