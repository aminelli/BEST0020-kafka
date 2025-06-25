package com.corso.kafka.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class ConsumerBase {

    protected <K, V> void printMetadata(ConsumerRecord<K, V> data, long messageIndex) {
        System.out.print  ("-".repeat(50));
        System.out.print  (" Msg Index : " + messageIndex);
        System.out.print  (" Topic     : " + data.topic());
        System.out.print  (" Partition : " + data.partition());
        System.out.print  (" Offset    : " + data.offset());
        System.out.println(" Timestamp : " + data.timestamp());
        System.out.print  (" Key       : " + data.key());
        System.out.println(" Value     : " + data.value());
    }

}
