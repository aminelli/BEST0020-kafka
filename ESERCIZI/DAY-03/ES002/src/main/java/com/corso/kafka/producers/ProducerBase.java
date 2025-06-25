package com.corso.kafka.producers;

import org.apache.kafka.clients.producer.RecordMetadata;

public class ProducerBase {
    
    protected void printMetadata(RecordMetadata data, int messageIndex) {
        System.out.print("-->");
        System.out.print(" Msg Index : " + messageIndex);
        System.out.print(" Topic     : " + data.topic());
        System.out.print(" Partition : " + data.partition());
        System.out.print(" Offset    : " + data.offset());
        System.out.println(" Timestamp : " + data.timestamp());
        
    }


}
