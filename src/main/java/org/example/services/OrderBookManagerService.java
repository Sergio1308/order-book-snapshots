package org.example.services;

import org.example.models.Ask;
import org.example.models.Bid;
import org.example.models.OrderBook;

import java.net.URL;
import java.util.Iterator;
import java.util.Locale;

public class OrderBookManagerService {

    private OrderBook previousOrderBook;

    public void processOrderBook(URL finalUrl) {
        JsonParser parser = new JsonParser();
        String jsonObj = parser.getJsonStringResponse(finalUrl);
        OrderBook orderBook = parser.parseJson(jsonObj);

        if (previousOrderBook != null) {
            writeComparisonOfBooksLog(orderBook);
            writeQtyDifferenceBetweenOrdersLog(orderBook);
        }
        previousOrderBook = orderBook;
    }

    private String compareTwoBooks(OrderBook orderBook) {
        StringBuilder builder = new StringBuilder();
        Iterator<Bid> bidsListIterator = orderBook.getBidsList().iterator();
        Iterator<Bid> previousBidsListIterator = previousOrderBook.getBidsList().iterator();
        Iterator<Ask> asksListIterator = orderBook.getAsksList().iterator();
        Iterator<Ask> previousAsksListIterator = previousOrderBook.getAsksList().iterator();

        // Checking hasNext only with asks because they have the largest (limit=asks.size) and...
        // ...unchanging length of array-elements compared to the previous request
        while (asksListIterator.hasNext()) {
            if (bidsListIterator.hasNext() && previousBidsListIterator.hasNext()) {
                Bid currentBid = bidsListIterator.next();
                Bid previousBid = previousBidsListIterator.next();
                String bidString = currentBid.comparePriceBetweenBooks(previousBid);
                if (bidString.isEmpty()) {
                    continue;
                }
                builder.append(bidString).append("\n");
            }
            Ask currentAsk = asksListIterator.next();
            Ask previousAsk = previousAsksListIterator.next();
            String askString = currentAsk.comparePriceBetweenBooks(previousAsk);
            if (askString.isEmpty()) {
                continue;
            }
            builder.append(askString).append("\n");
        }
        return builder.toString();
    }

    private void writeComparisonOfBooksLog(OrderBook orderBook) {
        String result = compareTwoBooks(orderBook);
        LoggerService.writeLogToFile(result);
    }

    private void writeQtyDifferenceBetweenOrdersLog(OrderBook orderBook) {
        LoggerService.writeLogToFile(String.format(
                Locale.US, "CUMULATIVE SIZE CHANGE: [bids] (%f), [asks] (%f)\n",
                orderBook.calculateQtyDifferenceBetweenOrders(previousOrderBook)[0],
                orderBook.calculateQtyDifferenceBetweenOrders(previousOrderBook)[1]));
    }
}
