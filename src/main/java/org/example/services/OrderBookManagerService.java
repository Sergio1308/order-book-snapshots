package org.example.services;

import org.example.models.Ask;
import org.example.models.Bid;
import org.example.models.OrderBook;

import java.util.Iterator;
import java.util.Locale;

public class OrderBookManagerService {

    private OrderBook previousOrderBook;

    public void processOrderBook(OrderBook orderBook) {
        // debug
        System.out.println(orderBook.getBidsList());
        System.out.println(orderBook.getAsksList());

        if (previousOrderBook != null) {
            Iterator<Bid> bidsListIterator = orderBook.getBidsList().iterator();
            Iterator<Bid> previousBidsListIterator = previousOrderBook.getBidsList().iterator();
            Iterator<Ask> asksListIterator = orderBook.getAsksList().iterator();
            Iterator<Ask> previousAsksListIterator = previousOrderBook.getAsksList().iterator();

            while (bidsListIterator.hasNext() && asksListIterator.hasNext()) {
                Bid currentBid = bidsListIterator.next();
                Ask currentAsk = asksListIterator.next();
                Bid previousBid = previousBidsListIterator.next();
                Ask previousAsk = previousAsksListIterator.next();
                // todo LOG
                System.out.println(currentBid.comparePriceBetweenBooks(previousBid));
                System.out.println(currentAsk.comparePriceBetweenBooks(previousAsk));
            }
            // TODO LOG
            System.out.printf(
                    Locale.US, "CUMULATIVE SIZE CHANGE: [bids] (%f), [asks] (%f)",
                    orderBook.calculateQtyDifferenceBetweenOrders(previousOrderBook)[0],
                    orderBook.calculateQtyDifferenceBetweenOrders(previousOrderBook)[1]
            );
        }
        previousOrderBook = orderBook;
    }
}
