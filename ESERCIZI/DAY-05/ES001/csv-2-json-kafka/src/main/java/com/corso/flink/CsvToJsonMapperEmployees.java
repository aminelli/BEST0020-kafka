package com.corso.flink;


import org.apache.flink.api.common.functions.MapFunction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CsvToJsonMapperEmployees implements MapFunction<String, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Configurazione delle colonne CSV (modifica secondo il tuo CSV)
    private static final String[] COLUMNS = {
            "employeeNumber",
            "lastName",
            "firstName",
            "extension",
            "email",
            "officeCode",
            "reportsTo",
            "jobTitle"
    };

    @Override
    public String map(String csvLine) throws Exception {
        try {




            // Parsing della riga CSV
            String[] values = csvLine.split(",");

            // Creazione dell'oggetto JSON
            ObjectNode jsonObject = objectMapper.createObjectNode();
            jsonObject.put("employeeNumber",   Integer.parseInt(values[0]));
            jsonObject.put("lastName",         values[1].trim());
            jsonObject.put("firstName",        values[2].trim());
            jsonObject.put("extension",        values[3].trim());
            jsonObject.put("email",            values[4].trim());
            jsonObject.put("officeCode",       values[5].trim());
            jsonObject.put("reportsTo",        values[6].trim());
            jsonObject.put("jobTitle",         values[7].trim());
            // Convert to JSON string

            // Aggiunta di timestamp di elaborazione
            jsonObject.put("processing_timestamp", System.currentTimeMillis());

            return objectMapper.writeValueAsString(jsonObject);

        } catch (Exception e) {
            // In caso di errore, restituisce un JSON con errore
            ObjectNode errorJson = objectMapper.createObjectNode();
            errorJson.put("error", "Failed to parse CSV line");
            errorJson.put("original_line", csvLine);
            errorJson.put("error_message", e.getMessage());
            errorJson.put("processing_timestamp", System.currentTimeMillis());

            return objectMapper.writeValueAsString(errorJson);
        }
    }
}