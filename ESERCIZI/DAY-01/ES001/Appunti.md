# Esercizio

```batch
REM Aprire un nuovo prompt dei comandi,
REM Posizionarsi sulla folder principale di Kafka
REM (Ovvero la folder dove si è scompattati il file zip di kafka)
REM e lanciare il comando:
bin\windows\kafka-storage.bat format --standalone -t 12345 -c config\kraft\server.properties

REM Lanciare ora il comando per far partire il server:
bin\windows\kafka-server-start.bat config\kraft\server.properties  
 
REM Aprire un nuovo prompt dei comandi,
REM Posizionarsi sulla folder principale di Kafka
REM (Ovvero la folder dove si è scompattati il file zip di kafka)
REM e lanciare il comando per creare il topic 
bin\windows\kafka-topics.bat --create --topic quickstart-events --bootstrap-server localhost:9092

REM Lanciare il comando per verificare la creazione del topic 
bin\windows\kafka-topics.bat --describe --topic quickstart-events --bootstrap-server localhost:9092

REM Lanciare il comando per avviare un producer 
bin\windows\kafka-console-producer.bat --topic quickstart-events --bootstrap-server localhost:9092

REM Aprire un nuovo prompt dei comandi,
REM Posizionarsi sulla folder principale di Kafka
REM (Ovvero la folder dove si è scompattati il file zip di kafka)
REM e lanciare il comando pre creare il consumer:
bin\windows\kafka-console-consumer.bat --topic quickstart-events --from-beginning --bootstrap-server localhost:9092
``` 