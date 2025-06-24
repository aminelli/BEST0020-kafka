# Esercizio

bin\windows\kafka-storage.bat format --standalone -t 12345 -c config\kraft\server.properties

bin\windows\kafka-server-start.bat config\kraft\server.properties  
 
bin\windows\kafka-topics.bat --create --topic quickstart-events --bootstrap-server localhost:9092
 
bin\windows\kafka-topics.bat --describe --topic quickstart-events --bootstrap-server localhost:9092
 
bin\windows\kafka-console-producer.bat --topic quickstart-events --bootstrap-server localhost:9092
 
bin\windows\kafka-console-consumer.bat --topic quickstart-events --from-beginning --bootstrap-server localhost:9092
 