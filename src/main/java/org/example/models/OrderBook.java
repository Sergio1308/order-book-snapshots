package org.example.models;

import java.util.List;

public class OrderBook {

    private final List<Bid> bidsList;
    private final List<Ask> asksList;

    public OrderBook(List<Bid> bidsList, List<Ask> asksList) {
        this.bidsList = bidsList;
        this.asksList = asksList;
    }

    /**
     * Calculate qty (size) difference between previous order book and current.
     * @param previousOrderBook previous order book, contains previous bids and asks.
     * @return array of double, where index 0 - bids difference, index 1 - asks difference.
     */
    public double[] calculateQtyDifferenceBetweenOrders(OrderBook previousOrderBook) {
        double currentBidsSum = sumOfListQty(bidsList);
        double currentAsksSum = sumOfListQty(asksList);
        double previousBidsSum = sumOfListQty(previousOrderBook.bidsList);
        double previousAsksSum = sumOfListQty(previousOrderBook.asksList);
        return new double[] {(previousBidsSum - currentBidsSum), (previousAsksSum - currentAsksSum)};
    }

    /**
     * Count a sum of qty (size) for all elements in List.
     * @param booksList list of bids or asks.
     * @return sum of qty in double.
     */
    public double sumOfListQty(List<? extends AbstractBook> booksList) {
        int size = booksList.size();
        if (size == 0) {
            return 0;
        } else {
            return booksList.get(0).getQty() + sumOfListQty(booksList.subList(1, size));
        }
    }

    public List<Ask> getAsksList() {
        return asksList;
    }

    public List<Bid> getBidsList() {
        return bidsList;
    }

    @Override
    public String toString() {
        return "OrderBook{" +
                "asksList=" + asksList +
                ", bidsList=" + bidsList +
                '}';
    }
}
