# Creazione producer in Java

## Java notes

Creare un progetto java con un archetype maven:

```shell
mvn archetype:generate -DgroupId=com.corso.kafka -DartifactId=kafka-producers -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DinteractiveMode=false
mvn clean install
```

NOTA:
Per chi ha problemi con la java_home:
```shell
SET PATH=%JAVA_HOME%\bin:%PATH%;
```