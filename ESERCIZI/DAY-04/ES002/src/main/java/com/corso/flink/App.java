package com.corso.flink;

import java.util.HashMap;
import java.util.Map;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.OutputFileConfig;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.OnCheckpointRollingPolicy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {
        
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        
        /* 
        String basePath = "D:\\Corsi\\Library\\Code\\Products\\Kafka\\corso\\BEST0020-kafka\\ESERCIZI\\DAY-04\\ES002\\datasets\\";    
        String basePathCsv = basePath + "csv\\";    
        String basePathJson = basePath + "json\\";  
        */

        /**/
        String basePath = "/datasets/";    
        String basePathCsv = basePath + "csv/";    
        String basePathJson = basePath + "json/";  
        

        String inputFile = "moviesDB.csv";
        String outputFile = "moviesDB";

        String fullPathCsv = basePathCsv + inputFile;    

        // Source
        FileSource<String> source =  FileSource
            .forRecordStreamFormat(
                new TextLineInputFormat(), 
                new Path(fullPathCsv)
            )
            .build();

        DataStreamSource<String> csvInput = env.fromSource(
            source, 
            WatermarkStrategy.noWatermarks(), 
            outputFile
        );

        ObjectMapper mapper = new ObjectMapper();

       
        var dataStream = csvInput
            .filter(line -> !line.startsWith("movie")) // skip header
            .map(line -> {
                //String[] fields = line.split(",");
                CSVParser csvParser = new CSVParserBuilder().withSeparator(',').build();
                String[] fields = csvParser.parseLine(line);
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("movie",        Integer.parseInt(fields[0]));
                jsonMap.put("title",        fields[1].trim());
                jsonMap.put("genres",       fields[2].trim());
                jsonMap.put("year",         Integer.parseInt(fields[3]));
                jsonMap.put("Rating",        Integer.parseInt(fields[4]));
                jsonMap.put("Rotton Tomato", Integer.parseInt(fields[5]));
                return mapper.writeValueAsString(jsonMap);
            });

        
        // Sink    

        FileSink<String> fileSink = FileSink
            .forRowFormat(
                new Path(basePathJson),
                new SimpleStringEncoder<String>("UTF-8")
            )
            .withRollingPolicy(OnCheckpointRollingPolicy.build())
            .withOutputFileConfig(
                OutputFileConfig.builder()
                .withPartPrefix("part")
                .withPartSuffix(".json")
                .build()                
            )
            .build();

        
        //jsonOutput.sinkTo(sink);
        dataStream.sinkTo(fileSink);
        
        //String brokers = "10.0.0.43:9092,10.0.0.43:9093,10.0.0.43:9094";
        //String brokers = "broker01:29092,broker02:29093,broker03:29094";
        String brokers = "172.20.0.5:9092,172.20.0.5:9093,172.20.0.5:9094";
        

        KafkaSink<String> kafkaSink = KafkaSink.<String>builder()
            .setBootstrapServers(brokers)
            .setRecordSerializer(
                KafkaRecordSerializationSchema.builder()
                    .setTopic("MOVIES")
                    .setValueSerializationSchema(new SimpleStringSchema())
                    .build()
            )
            .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
            .setTransactionalIdPrefix("flink-movies-")
            .build();
             
          
        dataStream.sinkTo(kafkaSink);

        env.execute("CSV 2 JON Source/Sink");

    }

}
