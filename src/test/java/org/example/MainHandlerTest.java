package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import org.example.domain.Currency;
import org.example.exception.ResourceNotFoundException;
import org.example.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainHandlerTest {

    private static final int SUCCESSFUL_STATUS_CODE = 200;
    private static final int NOT_FOUND_STATUS_CODE = 404;
    private static final String NOT_FOUND_MESSAGE = "Specified currency not found";
    private static final String NOT_SPECIFY_CURRENCY_MESSAGE = "Currency not specified";
    private static final String CURRENCY_NAME = "name";
    private static final String CURRENCY_PRICE = "$100,000";
    private static final String SUCCESSFUL_MESSAGE = "{\"name\":\"name\",\"price\":\"$100,000\"}";

    @Mock
    Context context;

    @Mock
    CurrencyService currencyService;

    @InjectMocks
    MainHandler instance;

    @Test
    void handleRequestShouldReturnSuccessfulStatusCode() throws ResourceNotFoundException {
        Map<String, Object> params = Map.of("queryStringParameters", Map.of("currency", CURRENCY_NAME));
        Currency currency = new Currency(CURRENCY_NAME, CURRENCY_PRICE);
        when(currencyService.getCurrencyByName(CURRENCY_NAME)).thenReturn(currency);

        APIGatewayV2HTTPResponse actual = instance.handleRequest(params, context);

        assertEquals(SUCCESSFUL_STATUS_CODE, actual.getStatusCode());
        assertEquals(SUCCESSFUL_MESSAGE, actual.getBody());
    }

    @Test
    void handleRequestShouldReturnNotFoundStatusCodeWhenCurrencyNotFound() throws ResourceNotFoundException {
        Map<String, Object> params = Map.of("queryStringParameters", Map.of("currency", CURRENCY_NAME));
        when(currencyService.getCurrencyByName(CURRENCY_NAME)).thenThrow(ResourceNotFoundException.class);

        APIGatewayV2HTTPResponse actual = instance.handleRequest(params, context);

        assertEquals(NOT_FOUND_STATUS_CODE, actual.getStatusCode());
        assertEquals(NOT_FOUND_MESSAGE, actual.getBody());
    }

    @Test
    void handleRequestShouldReturnSuccessfulStatusCodeWhenCurrencyNotSpecified() throws ResourceNotFoundException {
        Map<String, Object> params = Map.of("queryStringParameters", Collections.emptyMap());

        APIGatewayV2HTTPResponse actual = instance.handleRequest(params, context);

        assertEquals(NOT_FOUND_STATUS_CODE, actual.getStatusCode());
        assertEquals(NOT_SPECIFY_CURRENCY_MESSAGE, actual.getBody());
    }


}