package com.corso.kafka.producers;

import org.apache.kafka.clients.producer.RecordMetadata;

public class ProducerBase {
    
    protected void printMetadata(RecordMetadata data) {
        System.out.print("-".repeat(40));
        System.out.print("Topic     : " + data.topic());
        System.out.print("Partition : " + data.partition());
        System.out.print("Offset    : " + data.offset());
        System.out.print("Timestamp : " + data.timestamp());
        
    }


}
