package org.example.services;

import org.example.models.Ask;
import org.example.models.Bid;
import org.example.models.OrderBook;

import java.net.URL;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class OrderBookManagerService {

    private OrderBook previousOrderBook;

    private StringBuilder comparisonResult;

    public void processOrderBook(URL finalUrl) {
        JsonParser parser = new JsonParser();
        String jsonObj;
        OrderBook orderBook;
        try {
            jsonObj = parser.getJsonStringResponse(finalUrl);
            orderBook = parser.parseJsonToObject(jsonObj);
        } catch (InvalidUrlException | JsonParsingException e) {
            e.printStackTrace();
            LoggerService.writeLogToFile(e.toString());
            return;
        }
        if (previousOrderBook != null) {
            writeComparisonOfBooksLog(orderBook);
            writeQtyDifferenceBetweenOrdersLog(orderBook);
        }
        previousOrderBook = orderBook;
    }

    private String compareTwoBooks(OrderBook orderBook) {
        comparisonResult = new StringBuilder();
        Map<Double, Bid> previousBidsMap = previousOrderBook.getBidsMap();
        Map<Double, Ask> previousAsksMap = previousOrderBook.getAsksMap();
        Map<Double, Bid> currentBidsMap = orderBook.getBidsMap();
        Map<Double, Ask> currentAsksMap = orderBook.getAsksMap();

        Iterator<Map.Entry<Double, Bid>> bidsMapIterator = currentBidsMap.entrySet().iterator();
        Iterator<Map.Entry<Double, Bid>> previousBidsMapIterator = previousBidsMap.entrySet().iterator();
        Iterator<Map.Entry<Double, Ask>> asksMapIterator = currentAsksMap.entrySet().iterator();
        Iterator<Map.Entry<Double, Ask>> previousAsksMapIterator = previousAsksMap.entrySet().iterator();

        // Checking hasNext only with asks because they have the largest (limit=asks.size) and...
        // ...unchanging length of array-elements compared to the previous request
        while (asksMapIterator.hasNext() && previousAsksMapIterator.hasNext()) {
            // previous bids map and current bids map have different sizes (but <asks), so we check hasNext separately
            if (bidsMapIterator.hasNext()) {
                Map.Entry<Double, Bid> currentBid = bidsMapIterator.next();
                String bidString = orderBook.checkIfPriceLevelUpdatedOrAdded(previousBidsMap, currentBid.getValue());
                appendIfNotEmpty(bidString);
            }
            if (previousBidsMapIterator.hasNext()) {
                Map.Entry<Double, Bid> previousBid = previousBidsMapIterator.next();
                String bidString = orderBook.checkIfPriceLevelDeleted(currentBidsMap, previousBid.getValue());
                appendIfNotEmpty(bidString);
            }
            Map.Entry<Double, Ask> currentAsk = asksMapIterator.next();
            Map.Entry<Double, Ask> previousAsk = previousAsksMapIterator.next();
            String askString1 = orderBook.checkIfPriceLevelUpdatedOrAdded(previousAsksMap, currentAsk.getValue());
            String askString2 = orderBook.checkIfPriceLevelDeleted(currentAsksMap, previousAsk.getValue());
            appendIfNotEmpty(askString1, askString2);
        }
        return comparisonResult.toString();
    }

    private void appendIfNotEmpty(String... text) {
        for (String s : text) {
            if (!s.isEmpty()) comparisonResult.append(s).append("\n");
        }
    }

    private void writeComparisonOfBooksLog(OrderBook orderBook) {
        String result = compareTwoBooks(orderBook);
        LoggerService.writeLogToFile(result);
    }

    private void writeQtyDifferenceBetweenOrdersLog(OrderBook orderBook) {
        double[] result = orderBook.calculateQtyDifferenceBetweenOrderBooks(previousOrderBook);
        LoggerService.writeLogToFile(String.format(
                Locale.US, "CUMULATIVE SIZE CHANGE: [bids] (%.3f), [asks] (%.3f)\n", result[0], result[1]
        ));
    }
}
