
```shell

# 
spark-submit --master spark://spark-master:7077 --packages "org.apache.spark:spark-sql-kafka-0-10_2.13:4.0.0,org.apache.kafka:kafka-clients:4.0.0"

wget -O spark-sql-kafka-0-10_2.13-4.0.0.jar https://repo1.maven.org/maven2/org/apache/spark/spark-sql-kafka-0-10_2.13/4.0.0/spark-sql-kafka-0-10_2.13-4.0.0.jar
wget -O kafka-clients-4.0.0.jar  https://repo1.maven.org/maven2/org/apache/kafka/kafka-clients/4.0.0/kafka-clients-4.0.0.jar
```
 