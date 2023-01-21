package org.example.models;

import java.util.Map;

public class OrderBook {

    private final Map<Double, Bid> bidsMap;
    private final Map<Double, Ask> asksMap;

    public OrderBook(Map<Double, Bid> bidsMap, Map<Double, Ask> asksMap) {
        this.bidsMap = bidsMap;
        this.asksMap = asksMap;
    }

    /**
     * Calculate qty (size) difference between previous order book and current.
     * @param previousOrderBook previous order book, contains previous bids and asks.
     * @return array of double, where index 0 - bids difference, index 1 - asks difference.
     */
    public double[] calculateQtyDifferenceBetweenOrders(OrderBook previousOrderBook) {
        double currentBidsSum = sumOfMapQty(bidsMap);
        double currentAsksSum = sumOfMapQty(asksMap);
        double previousBidsSum = sumOfMapQty(previousOrderBook.bidsMap);
        double previousAsksSum = sumOfMapQty(previousOrderBook.asksMap);
        return new double[] {(previousBidsSum - currentBidsSum), (previousAsksSum - currentAsksSum)};
    }

    /**
     * Count a sum of qty (size) for all values in Map.
     * @param bookMap map of bids or asks.
     * @return sum of qty in double.
     */
    public double sumOfMapQty(Map<Double, ? extends AbstractBook> bookMap) {
        return bookMap.values().stream().mapToDouble(AbstractBook::getQty).sum();
    }

    public Map<Double, Bid> getBidsMap() {
        return bidsMap;
    }

    public Map<Double, Ask> getAsksMap() {
        return asksMap;
    }

    @Override
    public String toString() {
        return "OrderBook{" +
                "bidsMap=" + bidsMap +
                ", asksMap=" + asksMap +
                '}';
    }
}
