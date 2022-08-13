#!/bin/bash
echo "Subindo Aplicação"
#mvn clean install
docker compose -f "$PWD"/run/docker-compose.yaml up --build

