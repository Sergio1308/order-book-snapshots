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

        while (bidsListIterator.hasNext() && asksListIterator.hasNext()) {
            Bid currentBid = bidsListIterator.next();
            Ask currentAsk = asksListIterator.next();
            Bid previousBid = previousBidsListIterator.next();
            Ask previousAsk = previousAsksListIterator.next();

            String bidString = currentBid.comparePriceBetweenBooks(previousBid);
            String askString = currentAsk.comparePriceBetweenBooks(previousAsk);
            if (bidString.isEmpty() || askString.isEmpty()) {
                continue;
            }
            builder.append(bidString).append("\n").append(askString).append("\n");
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
