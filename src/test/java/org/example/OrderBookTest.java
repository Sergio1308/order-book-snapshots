package org.example;

import org.example.models.Ask;
import org.example.models.Bid;
import org.example.models.OrderBook;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderBookTest {

    private Map<Double, Bid> bidsMap;
    private Map<Double, Ask> asksMap;

    private OrderBook orderBook;

    @BeforeEach
    void setUp() {
        bidsMap = new HashMap<>();
        asksMap = new HashMap<>();

        bidsMap.put(6.772, new Bid(6.772, 18.1));
        bidsMap.put(6.1, new Bid(6.1, 20.4));
        asksMap.put(1.1, new Ask(1.1, 5.5));
        asksMap.put(4.45, new Ask(4.45, 2.5));
        asksMap.put(10.1, new Ask(10.1, 10.5));

        orderBook = new OrderBook(bidsMap, asksMap);
    }

    @Test
    void checkIfPriceLevelUpdatedOrAddedOrDeletedTest() {
        String actualResult1 = orderBook.checkIfPriceLevelUpdatedOrAdded(bidsMap, new Bid(6.772, 12.3));
        String actualResult2 = orderBook.checkIfPriceLevelUpdatedOrAdded(bidsMap, new Bid(6.2, 10.6));
        String actualResult3 = orderBook.checkIfPriceLevelDeleted(bidsMap, new Bid(6.2, 10.6));
        String actualResult4 = orderBook.checkIfPriceLevelUpdatedOrAdded(asksMap, new Ask(4.44, 2.5));
        String actualResult5 = orderBook.checkIfPriceLevelDeleted(asksMap, new Ask(4.44, 2.5));

        assertEquals("update [Bids] (6.772, 12.300)", actualResult1);
        assertEquals("new [Bids] (6.200, 10.600)", actualResult2);
        assertEquals("delete [Bids] (6.200, 10.600)", actualResult3);
        assertEquals("new [Asks] (4.440, 2.500)", actualResult4);
        assertEquals("delete [Asks] (4.440, 2.500)", actualResult5);
    }

    @Test
    void calculateQtyDifferenceBetweenOrderBooksTest() {
        double[] actualResult = orderBook.calculateQtyDifferenceBetweenOrderBooks(
                new OrderBook(
                        Map.of(
                                6.772, new Bid(6.772, 12.1),
                                6.2, new Bid(6.2, 10.6)),
                        Map.of(
                                1.1, new Ask(1.1, 5.5),
                                4.44, new Ask(4.44, 2.5),
                                2.8, new Ask(2.8, 1.2)
                              )));
        assertArrayEquals(new double[] {-15.8, -9.3}, actualResult);
    }
}
