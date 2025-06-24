# Docker Kafka Test


## Step 1 - Creazione Rete

```shell
docker network create net-kafka
```

## Step 2 - Creazione container con istanza Kafka (standalone)

```shell
# Creazione container
docker run -d --hostname kafka01 --name kafka01 -p 9092:9092 --network net-kafka apache/kafka:3.9.1

# Verifichiamo i log del container
docker logs -f kafka01
```

## Step 3 - Creazione Topic

```shell
# Aggangio al container di kafka; viene avviato un nuovo pid che attiva la bash
docker exec -it kafka01 /bin/bash

# mi sposto nella cartella del container dove risiedono i tools a linea di comando di kafka
cd opt/kafka/bin/

# Creazione Topic
./kafka-topics.sh --create --topic test-corso --bootstrap-server localhost:9092

# Verifica Creazione Topic
./kafka-topics.sh --describe --topic test-corso --bootstrap-server localhost:9092
```
