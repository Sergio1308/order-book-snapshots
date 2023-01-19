package org.example.models;

import java.util.Locale;

public abstract class AbstractBook {

    private final double price;
    private final double qty;

    public AbstractBook(double price, double qty) {
        this.price = price;
        this.qty = qty;
    }

    public abstract String getSide();

    public double getPrice() {
        return price;
    }

    public double getQty() {
        return qty;
    }

    /**
     * Difference in price between the previous and current book.
     * @param previousBook Bid or Ask object.
     * @return result string
     */
    public String comparePriceBetweenBooks(AbstractBook previousBook) {
        if (price != previousBook.getPrice()) {
            return String.format(Locale.US,"delete[%s] (%f, %f)\t->\tnew[%s] (%f, %f)",
                    previousBook.getSide(),
                    previousBook.getPrice(),
                    previousBook.getQty(),
                    getSide(),
                    getPrice(),
                    getQty());
        } else if (price == previousBook.getPrice() && qty != previousBook.getQty()) {
            return String.format(Locale.US,"update[%s] (%f, %f)",
                    getSide(),
                    getPrice(),
                    getQty());
        }
        return "";
    }

    @Override
    public String toString() {
        return getSide() + "{" +
                "price=" + price +
                ", qty=" + qty +
                '}';
    }
}
