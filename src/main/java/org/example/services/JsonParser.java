package org.example.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.Ask;
import org.example.models.Bid;
import org.example.models.OrderBook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class JsonParser {

    /**
     * Sending GET request.
     * @param url URL.
     * @return JSON as a String.
     */
    public String getJsonStringResponse(URL url) throws InvalidUrlException {
        String readLine;
        HttpURLConnection connection;
        int responseCode;
        StringBuilder response = new StringBuilder();
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((readLine = bufferedReader.readLine()) != null) {
                    response.append(readLine);
                }
                bufferedReader.close();
            } else {
                throw new InvalidUrlException("Received an invalid response code " + responseCode);
            }
        } catch (IOException e) {
            throw new InvalidUrlException("Invalid URL -> " + url);
        }
        return response.toString();
    }

    /**
     * Parse JSON String specific data.
     * @param jsonData JSON String.
     * @return OrderBook object filled with data.
     */
    public OrderBook parseJsonToObject(String jsonData) throws JsonParsingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(jsonData);
        } catch (JsonProcessingException e) {
            throw new JsonParsingException("Cannot parse JSON -> " + jsonData);
        }
        Map<Double, Bid> bidsMap = new HashMap<>();
        Map<Double, Ask> asksMap = new HashMap<>();

        JsonNode bidsNode = rootNode.path("bids");
        JsonNode asksNode = rootNode.path("asks");
        Iterator<JsonNode> bidsIterator = bidsNode.elements();
        Iterator<JsonNode> asksIterator = asksNode.elements();
        // Bids and asks have a different number of array-elements, so we check hasNext in bids OR asks
        while (bidsIterator.hasNext() || asksIterator.hasNext()) {
            if (bidsIterator.hasNext()) {
                JsonNode currentBid = bidsIterator.next();
                bidsMap.put(currentBid.get(0).asDouble(),
                        new Bid(currentBid.get(0).asDouble(), currentBid.get(1).asDouble()));
            } else {
                JsonNode currentAsk = asksIterator.next();
                asksMap.put(currentAsk.get(0).asDouble(),
                        new Ask(currentAsk.get(0).asDouble(), currentAsk.get(1).asDouble()));
            }
        }
        return new OrderBook(bidsMap, asksMap);
    }
}
