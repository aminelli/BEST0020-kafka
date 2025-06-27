package com.corso.flink;

import java.time.Duration;
import java.util.Properties;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.OutputFileConfig;
import org.apache.flink.streaming.api.functions.sink.filesystem.legacy.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.OnCheckpointRollingPolicy;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class App {

    // Kafka configuration
    private static final String KAFKA_BOOTSTRAP_SERVERS = "broker01:9092,broker02:9092,broker03:9092";
    //private static final String KAFKA_BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";
    private static final String KAFKA_TOPIC = "EMPLOYEES";


    public static void main(String[] args) throws Exception {
        
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Configurazione del checkpoint per la fault tolerance
        env.enableCheckpointing(5000);

        // Configurazione del parallelismo
        env.setParallelism(2);

        /* 
        String basePath = "D:\\Corsi\\Library\\Code\\Products\\Kafka\\docker\\Kafka-Flink-Cluster\\flink\\datasets\\";
        String basePathCsv = basePath + "csv\\";    
        String basePathJson = basePath + "json\\";  
        */

        /* */
        String basePath = "/datasets/";    
        String basePathCsv = basePath + "csv/";    
        String basePathJson = basePath + "json/";  
        

        String inputFile = "employees.csv";
        String outputFile = "employees";

        String fullPathCsv = basePathCsv + inputFile;
        String fullPathCsvTest = basePathCsv + "employeesTest.csv";


        EnvironmentSettings settings = EnvironmentSettings
          .newInstance()
          .inStreamingMode()
          .build();

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env,settings);

        String createTable = String.format("""
                        CREATE TABLE employeesTest (
                             employeeNumber INT,
                             lastName STRING,
                             firstName STRING,
                             extension STRING,
                             email STRING,
                             officeCode STRING,
                             reportsTo STRING,
                             jobTitle STRING  
                        ) WITH (
                                'connector' = 'filesystem',
                                'path' = 'file://%s',
                                'format' = 'csv',
                                'csv.ignore-parse-errors' = 'true',
                                'csv.allow-comments' = 'true'
                        )                        
                        """,
                        fullPathCsvTest
                        );

                tableEnv.executeSql(createTable);

                String querySQL = """
                                SELECT 
                                        jobTitle,
                                        COUNT(*) as employeeCount
                                FROM employeesTest
                                GROUP BY jobTitle
                                ORDER BY jobTitle DESC
                                """;

                Table result = tableEnv.sqlQuery(querySQL);
        

        // Configurazione del FileSource per leggere il CSV
        FileSource<String> source  =  FileSource
            .forRecordStreamFormat(
                new TextLineInputFormat(), 
                new Path(fullPathCsv)
            )
            .monitorContinuously(Duration.ofSeconds(30))
            .build();

        // Lettura del file CSV
        DataStream<String> csvStream = env.fromSource(
                source ,
                WatermarkStrategy.noWatermarks(),
                "csv-source");

        // Conversione da CSV a JSON
        DataStream<String> jsonStream = csvStream
                .filter(line -> !line.trim().isEmpty() && !line.startsWith("employeeNumber"))
                //.process(new CsvToJsonProcessor())
                //.map(new CsvToJsonMapperMovies())
                .map(new CsvToJsonMapperEmployees())
                .name("CSV to JSON Converter");


        StreamingFileSink<String> fileSink = StreamingFileSink
                .forRowFormat(
                        new Path(basePathJson),
                        //new SimpleStringSchema()
                        new SimpleStringEncoder<String>("UTF-8")
                )
                .withRollingPolicy(OnCheckpointRollingPolicy.build())
                //.withRollingPolicy(
                //        DefaultRollingPolicy.builder()
                //                .withRolloverInterval(TimeUnit.MINUTES.toMillis(1))
                //                .withInactivityInterval(TimeUnit.SECONDS.toMillis(30))
                //                .withMaxPartSize(1024 * 1024 * 1024) // 1GB
                //                .build()
                //)
                .withOutputFileConfig(
                        OutputFileConfig.builder()
                                .withPartPrefix("part")
                                .withPartSuffix(".json")
                                .build()
                )
                .build();

        jsonStream.addSink(fileSink).name("JSON to File");

        Properties props = new Properties();
        //props.setProperty("bootstrap.servers", KAFKA_BOOTSTRAP_SERVERS);
        props.setProperty("transaction.timeout.ms", "600000"); // Increase transaction timeout
        props.setProperty("delivery.timeout.ms", "120000"); // Match your batch timeout
        props.setProperty("request.timeout.ms", "30000");
        props.setProperty("max.block.ms", "60000");

        // Configure Kafka sink
        KafkaSink<String> kafkaSink = KafkaSink.<String>builder()
                .setBootstrapServers(KAFKA_BOOTSTRAP_SERVERS)
                //.setKafkaProducerConfig(props)
                .setRecordSerializer(
                        KafkaRecordSerializationSchema.builder()
                            .setTopic(KAFKA_TOPIC)
                            .setValueSerializationSchema(new SimpleStringSchema())
                            .build()
                )
                //.setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();
                // Send data to Kafka
        jsonStream.sinkTo(kafkaSink).name("Kafka Sink");



        // Execute the Flink job
        env.execute("CSV to JSon to Kafka Pipeline");

    }

}
