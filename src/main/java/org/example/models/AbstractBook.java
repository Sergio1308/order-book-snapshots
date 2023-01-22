package org.example.models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBook book = (AbstractBook) o;
        return Double.compare(book.price, price) == 0 && Double.compare(book.qty, qty) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, qty);
    }

    @Override
    public String toString() {
        return getSide() + "{" +
                "price=" + price +
                ", qty=" + qty +
                '}';
    }
}
