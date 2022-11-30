package org.example.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import org.example.domain.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CurrencyDAOTest {

    private static final String CURRENCY_NAME = "name";
    private static final String CURRENCY_PRICE = "$100,000";

    @Mock
    AmazonDynamoDB amazonDynamoDB;

    @InjectMocks
    CurrencyDAO instance;

    @Test
    void saveCurrency() {
        Currency currency = new Currency(CURRENCY_NAME, CURRENCY_PRICE);
        doReturn(new PutItemResult()).when(amazonDynamoDB).putItem(anyString(), anyMap());
        instance.saveCurrency(currency);
        verify(amazonDynamoDB).putItem(anyString(), anyMap());
    }
}