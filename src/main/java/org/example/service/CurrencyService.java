package org.example.service;

import org.example.domain.Currency;
import org.example.exception.ResourceNotFoundException;

import java.io.IOException;

public class CurrencyService {

    private final Parser parser;

    public CurrencyService(Parser parser) {
        this.parser = parser;
    }

    public Currency getCurrencyByName(String name) throws ResourceNotFoundException {
        try {
            String price = parser.getCurrencyPrice(name);
            return new Currency(name, price);
        }catch (IOException ex){
            throw new ResourceNotFoundException();
        }

    }
}
