package com.corso.flink;

import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CsvToJsonProcessor extends ProcessFunction<String, String> {
    private static final long serialVersionUID = 1L;
    private transient ObjectMapper mapper;

    @Override
    public void open(OpenContext openContext) throws Exception {
        mapper = new ObjectMapper();
    }

    @Override
    public void processElement(String value, Context ctx, Collector<String> out) throws Exception {
        // Skip header or empty lines
        if (value == null || value.isEmpty() || value.startsWith("movie")) {
            return;
        }

        // Parse CSV line (assuming format: id,amount,timestamp,category)
        String[] fields = value.split(",");
        if (fields.length >= 4) {
            // Create JSON object
            ObjectNode jsonNode = mapper.createObjectNode();
            jsonNode.put("movie",         Integer.parseInt(fields[0]));
            jsonNode.put("title",         fields[1].trim());
            jsonNode.put("genres",        fields[2].trim());
            jsonNode.put("year",          Integer.parseInt(fields[3]));
            jsonNode.put("Rating",        Integer.parseInt(fields[4]));
            jsonNode.put("Rotton Tomato", Integer.parseInt(fields[5]));
            // Convert to JSON string
            out.collect(mapper.writeValueAsString(jsonNode));
        }
    }
}
