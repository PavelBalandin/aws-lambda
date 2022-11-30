package org.example.service;

import org.example.dao.CurrencyDAO;
import org.example.domain.Currency;
import org.example.exception.ResourceNotFoundException;

import java.io.IOException;

public class CurrencyService {

    private final Parser parser;

    private CurrencyDAO currencyDAO;

    public CurrencyService(Parser parser, CurrencyDAO currencyDAO) {
        this.parser = parser;
        this.currencyDAO = currencyDAO;
    }

    public Currency getCurrencyByName(String name) throws ResourceNotFoundException {
        try {
            String price = parser.getCurrencyPrice(name);
            Currency currency = new Currency(name, price);
            currencyDAO.saveCurrency(currency);
            return currency;
        }catch (IOException ex){
            throw new ResourceNotFoundException();
        }

    }
}
