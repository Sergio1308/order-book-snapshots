package org.example.models;

public class Ask extends AbstractBook {

    public Ask(double price, double qty) {
        super(price, qty);
    }

    @Override
    public String getSide() {
        return "Asks";
    }
}
