package org.example.models;

import java.util.Locale;
import java.util.Map;

public class OrderBook {

    private final Map<Double, Bid> bidsMap;
    private final Map<Double, Ask> asksMap;

    public OrderBook(Map<Double, Bid> bidsMap, Map<Double, Ask> asksMap) {
        this.bidsMap = bidsMap;
        this.asksMap = asksMap;
    }

    /**
     * Difference in price between the previous and current request.
     * Checks if the price of the current order book (bids/asks) was updated or added compared to previous order book.
     * @param previousBookMap map, contains previous bids or asks.
     * @param currentBook current bid/ask object to check.
     * @return result string.
     */
    public String checkIfPriceLevelUpdatedOrAdded(Map<Double, ? extends AbstractBook> previousBookMap,
                                                  AbstractBook currentBook) {
        if (!previousBookMap.containsKey(currentBook.getPrice())) {
            return String.format(
                    Locale.US, "new [%s] (%.3f, %.3f)",
                    currentBook.getSide(),
                    currentBook.getPrice(),
                    currentBook.getQty());
        } else {
            if (previousBookMap.get(currentBook.getPrice()).getQty() != currentBook.getQty()) {
                return String.format(
                        Locale.US,"update [%s] (%.3f, %.3f)",
                        currentBook.getSide(),
                        currentBook.getPrice(),
                        currentBook.getQty());
            }
        }
        return "";
    }

    /**
     * Difference in price between the previous and current book.
     * Checks if the price of the previous order book (bids/asks) exists in current order book.
     * @param currentBookMap map, contains current bids or asks.
     * @param previousBook previous bid/ask object to check.
     * @return result string.
     */
    public String checkIfPriceLevelDeleted(Map<Double, ? extends AbstractBook> currentBookMap,
                                           AbstractBook previousBook) {
        if (!currentBookMap.containsKey(previousBook.getPrice())) {
            return String.format(
                    Locale.US,"delete [%s] (%.3f, %.3f)",
                    previousBook.getSide(),
                    previousBook.getPrice(),
                    previousBook.getQty());
        }
        return "";
    }

    /**
     * Calculate qty (size) difference between previous order book and current.
     * @param previousOrderBook previous order book, contains previous bids and asks.
     * @return array of double, where index 0 - bids difference, index 1 - asks difference.
     */
    public double[] calculateQtyDifferenceBetweenOrderBooks(OrderBook previousOrderBook) {
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
