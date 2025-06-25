package com.corso.kafka.producers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

// Ack 1
public class ProducerSyncAckAll extends ProducerBase {
    
    public void sendMessages(String topicName, int maxMessages) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Properties props = new Properties();

        // Settiamo la connessione al cluster
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");

        // Client ID
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "Producer001");

        // Fattore di compressione dei messaggi
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");

        // Tipo Acks
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        // Namespaces delle CLASSI da utilizzare per la serializzazione della KEY e del
        // VALORE
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        ProducerRecord<String, String> record = null;

        
        String startProcess = formatter.format(new Date());

        for (int count = 0; count < maxMessages; count++) {
            String key = "K" + count;
            String value = "Ciao Msg nr " + count;
            record = new ProducerRecord<>(topicName, key, value);
            try {
                String headInfo = "MSG" + count;
                record.headers().add("INFO-MSG", headInfo.getBytes());
         
                Future<RecordMetadata> future = producer.send(record);
         
                RecordMetadata metadata = future.get();
                printMetadata(metadata, count);
         
                //System.out.println("Inviato MSG nr " + count);
            } catch (Exception ex) {
                 System.out.println("Errore su Invio MSG nr " + count);
            }
        }

        String endProcess = formatter.format(new Date());

        System.out.println("START : " + startProcess);
        System.out.println("END   : " + endProcess);

        producer.flush();
        producer.close();
    }

}
