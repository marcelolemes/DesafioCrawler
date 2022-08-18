#!/bin/bash
echo "Iniciando!"
echo "Limpando a pasta target!"
sudo mvn clean
echo "Montando pacote..."
mvn install
echo "Iniciando docker ..."
sudo service docker start
echo "Executando imagem no docker ..."
docker compose -f "$PWD"/run/docker-compose.yaml up --build

