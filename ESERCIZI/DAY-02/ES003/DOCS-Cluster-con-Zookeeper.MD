# Kafka Cluster con Zookeeper

## Step 1 - Creazione Rete

```shell
docker network create net-kafka
```

## Step 2 - Creazione Containers

```shell

docker run -d --hostname broker01 --name broker01 -p 9092:9092 -p 9101:9101 --network net-kafka --env-file .env.kafka01 confluentinc/cp-server:7.8.0
docker run -d --hostname broker02 --name broker02 -p 9093:9093 -p 9102:9101 --network net-kafka --env-file .env.kafka02 confluentinc/cp-server:7.8.0
docker run -d --hostname broker03 --name broker03 -p 9094:9094 -p 9103:9101 --network net-kafka --env-file .env.kafka03 confluentinc/cp-server:7.8.0

docker run -d --hostname kafka-ui --name kafka-ui -p 9088:8080 -e DYNAMIC_CONFIG_ENABLED=true --network net-kafka provectuslabs/kafka-ui

```