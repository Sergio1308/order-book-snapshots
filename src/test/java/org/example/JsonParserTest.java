package org.example;

import org.example.config.Config;
import org.example.models.OrderBook;
import org.example.services.InvalidUrlException;
import org.example.services.JsonParser;
import org.example.services.JsonParsingException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class JsonParserTest {

    private final JsonParser jsonParser = new JsonParser();

    @Test
    void getJsonStringResponseTest() throws IOException {
        String path = "https://api.binance.com/api/v3/depth?symbol=LINKUSDT&limit=1";
        String actualResult = jsonParser.getJsonStringResponse(new URL(path));
        assertEquals("{\"lastUpdateId\"", actualResult.substring(0, 15));
    }

    @Test
    void parseJsonToObjectTest() throws IOException {
        String jsonExample = "{\"lastUpdateId\":7441418149,\"bids\":[[\"6.77100000\",\"312.52000000\"]]," +
                "\"asks\":[[\"6.77200000\",\"197.65000000\"]]}";
        OrderBook actualResult = jsonParser.parseJsonToObject(jsonExample);
        assertEquals("{6.771=Bids{price=6.771, qty=312.52}}", actualResult.getBidsMap().toString());
        assertEquals("{6.772=Asks{price=6.772, qty=197.65}}", actualResult.getAsksMap().toString());
    }

    @Test
    void exceptionsTesting() {
        Exception malformedURLException = assertThrows(MalformedURLException.class, () -> {
            jsonParser.getJsonStringResponse(new URL("googl.com/"));
        });
        Exception invalidUrlException = assertThrows(InvalidUrlException.class, () -> {
            jsonParser.getJsonStringResponse(new URL("https://google."));
        });
        Exception jsonParsingException = assertThrows(JsonParsingException.class, () -> {
            jsonParser.parseJsonToObject("this is not json object");
            jsonParser.parseJsonToObject("{ 'empty': json }");
        });
        assertTrue(malformedURLException.getMessage().contains("no protocol"));
        assertTrue(invalidUrlException.getMessage().contains("Invalid URL"));
        assertTrue(jsonParsingException.getMessage().contains("Cannot parse JSON"));
    }
}
