package org.example.service;

import org.example.dao.CurrencyDAO;
import org.example.domain.Currency;
import org.example.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    private static final String CURRENCY_NAME = "name";
    private static final String CURRENCY_PRICE = "$100,000";

    @Mock
    Parser parser;

    @Mock
    CurrencyDAO currencyDAO;

    @InjectMocks
    CurrencyService instance;

    @Test
    void getCurrencyByNameShouldReturnCurrency() throws ResourceNotFoundException, IOException {
        when(parser.getCurrencyPrice(CURRENCY_NAME)).thenReturn(CURRENCY_PRICE);
        Currency expected = new Currency(CURRENCY_NAME, CURRENCY_PRICE);

        Currency actual = instance.getCurrencyByName(CURRENCY_NAME);

        assertEquals(expected, actual);
    }

    @Test
    void getCurrencyByNameShouldThrowExceptionWhenResourceNotFound() throws IOException {
        when(parser.getCurrencyPrice(CURRENCY_NAME)).thenThrow(IOException.class);

        assertThrows(ResourceNotFoundException.class, () -> instance.getCurrencyByName(CURRENCY_NAME));
    }
}