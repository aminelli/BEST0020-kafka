# Docker Kafka Test


## Step 1 - Creazione Rete

```shell
docker network create net-kafka
```

## Step 2 - Creazione container con istanza Kafka (standalone)

```shell
# Creazione container
docker run -d --hostname kafka01 --name kafka01 -p 9092:9092 --newtwork net-kafka apache/kafka:3.9.1

# Verifichiamo i log del container
docker logs -f kafka01
```
