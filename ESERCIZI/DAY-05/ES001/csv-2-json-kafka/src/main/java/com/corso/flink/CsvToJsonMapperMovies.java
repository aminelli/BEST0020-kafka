package com.corso.flink;

import org.apache.flink.api.common.functions.MapFunction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CsvToJsonMapperMovies implements MapFunction<String, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Configurazione delle colonne CSV (modifica secondo il tuo CSV)
    private static final String[] COLUMNS = {
            "movie",
            "title",
            "genres",
            "year",
            "Rating",
            "Rotton Tomato"
    };

    @Override
    public String map(String csvLine) throws Exception {
        try {




            // Parsing della riga CSV
            String[] values = csvLine.split(",");

            // Creazione dell'oggetto JSON
            ObjectNode jsonObject = objectMapper.createObjectNode();
            jsonObject.put("movie",         Integer.parseInt(values[0]));
            jsonObject.put("title",         values[1].trim());
            jsonObject.put("genres",        values[2].trim());
            jsonObject.put("year",          Integer.parseInt(values[3]));
            jsonObject.put("Rating",        Integer.parseInt(values[4]));
            jsonObject.put("Rotton Tomato", Integer.parseInt(values[5]));
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