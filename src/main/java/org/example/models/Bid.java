package org.example.models;

public class Bid extends AbstractBook {

    public Bid(double price, double qty) {
        super(price, qty);
    }

    @Override
    public String getSide() {
        return "Bids";
    }
}
