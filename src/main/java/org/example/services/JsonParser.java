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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonParser {

    /**
     * Sending GET request.
     * @param url URL.
     * @return JSON as a String.
     */
    public String getJsonStringResponse(URL url) {
        String readLine;
        HttpURLConnection connection;
        int responseCode;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                while ((readLine = bufferedReader.readLine()) != null) {
                    response.append(readLine);
                }
                bufferedReader.close();
                return response.toString();
            } else {
                return "Received an invalid response code " + responseCode;
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Parse JSON String specific data.
     * @param jsonData JSON String.
     * @return OrderBook object filled with data.
     */
    public OrderBook parseJson(String jsonData) {
        final ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(jsonData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        List<Bid> bidsList = new ArrayList<>();
        List<Ask> asksList = new ArrayList<>();

        JsonNode bidsNode = rootNode.path("bids");
        JsonNode asksNode = rootNode.path("asks");
        Iterator<JsonNode> bidsIterator = bidsNode.elements();
        Iterator<JsonNode> asksIterator = asksNode.elements();
        while (bidsIterator.hasNext() && asksIterator.hasNext()) {
            JsonNode currentBid = bidsIterator.next();
            JsonNode currentAsk = asksIterator.next();
            bidsList.add(new Bid(currentBid.get(0).asDouble(), currentBid.get(1).asDouble()));
            asksList.add(new Ask(currentAsk.get(0).asDouble(), currentAsk.get(1).asDouble()));
        }
        return new OrderBook(bidsList, asksList);
    }
}
