package org.example;

import org.example.config.Config;
import org.example.models.OrderBook;
import org.example.services.JsonParser;
import org.example.services.OrderBookManagerService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class App {
    public static void main( String[] args ) {
        String path = Config.HOST_URL + Config.REQUEST_URI + "?symbol=" + Config.SYMBOL + "&limit=" + Config.LIMIT;
        run(path);
    }

    public static void run(String path) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        JsonParser parser = new JsonParser();
        OrderBookManagerService bookManagerService = new OrderBookManagerService();
        URL url;
        try {
            url = new URL(path);
            URL finalUrl = url;
            final Runnable runnable = () -> {
                String jsonObj = parser.getJsonStringResponse(finalUrl);
                OrderBook orderBook = parser.parseJson(jsonObj);
                bookManagerService.processOrderBook(orderBook);
            };
            executorService.scheduleAtFixedRate(runnable, 0, Config.EXECUTION_PERIOD, TimeUnit.SECONDS);
        } catch (MalformedURLException e ) {
            e.printStackTrace();
        }
    }
}