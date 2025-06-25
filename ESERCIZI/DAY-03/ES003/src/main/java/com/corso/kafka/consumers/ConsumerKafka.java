package com.corso.kafka.consumers;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ConsumerKafka extends ConsumerBase {
    

    public void receiveMessages(String topicName) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Properties props = new Properties();

        // Settiamo la connessione al cluster
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");

        // Gruppo Consumer
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "GRNTT");

        // Namespaces delle CLASSI da utilizzare per la serializzazione della KEY e del
        // VALORE
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        List<String> topics = Collections.singletonList(topicName);
        
        consumer.subscribe(topics);

        try {
            Long countPollings = 0l;
            Long countRecords = 0l;

            do {
                countPollings++;

                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));


                for(ConsumerRecord<String, String> record : records) {
                    countRecords++;
                    printMetadata(record, countRecords);
                }

            } while (true);


        } catch (Exception ex) {
            System.out.println("Errore lettura messaggi");
        } finally {
            consumer.close();
        }
    }

}
