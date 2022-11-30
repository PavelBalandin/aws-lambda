package org.example.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SystemStubsExtension.class)
class ParserTest {

    private static final String CURRENCY_NAME = "name";
    private static final String CURRENCY_PRICE = "$100,000";
    private static final String CURRENCY_SELECTOR = "CURRENCY_SELECTOR";
    private static final String CURRENCY_URL_PATTERN = "CURRENCY_URL_PATTERN";
    private static final String CURRENCY_SELECTOR_VALUE = "div.priceValue>span";
    private static final String CURRENCY_URL_PATTERN_VALUE = "https://coinmarketcap.com/currencies/%s/";
    private static final String CURRENCY_NOT_EXISTENCE_NAME = "noname";
    private static final String WRONG_CURRENCY_URL_VALUE = "https://coinmarketcap.com/currencies/noname/";
    private static final String HTML = "<div class=\"priceValue \"><span>$100,000</span></div>";

    @SystemStub
    EnvironmentVariables environmentVariables;

    Parser instance;

    @BeforeEach
    void setUp(){
        environmentVariables.set(CURRENCY_SELECTOR, CURRENCY_SELECTOR_VALUE);
        environmentVariables.set(CURRENCY_URL_PATTERN, CURRENCY_URL_PATTERN_VALUE);
        instance = new MockParser();
    }

    @Test
    void getCurrencyPriceShouldReturnPriceByName() throws IOException {
        String price = instance.getCurrencyPrice(CURRENCY_NAME);

        assertEquals(CURRENCY_PRICE, price);
    }

    @Test
    void getCurrencyPriceShouldThrowIOExceptionWhenNameDoesNotExit() {
        assertThrows(IOException.class, () -> instance.getCurrencyPrice(CURRENCY_NOT_EXISTENCE_NAME));
    }

    static class MockParser extends Parser{
        @Override
        protected Document getDocumentByURL(String url) throws IOException {
            if(WRONG_CURRENCY_URL_VALUE.equals(url)){
                throw new IOException();
            }
            return Jsoup.parse(HTML);
        }
    }

}