package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.example.dao.CurrencyDAO;
import org.example.dao.DynamoDBSource;
import org.example.domain.Currency;
import org.example.exception.ResourceNotFoundException;
import org.example.service.CurrencyService;
import org.example.service.Parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class MainHandler implements RequestHandler<Map<String, Object>, APIGatewayV2HTTPResponse> {

    private static final int SUCCESSFUL_STATUS_CODE = 200;
    private static final int NOT_FOUND_STATUS_CODE = 404;
    private static final String NOT_FOUND_MESSAGE = "Specified currency not found";
    private static final String NOT_SPECIFY_CURRENCY_MESSAGE = "Currency not specified";
    private static final String RAW_QUERY_STRING = "queryStringParameters";
    private static final String CURRENCY_PARAM = "currency";

    private CurrencyService currencyService;

    public MainHandler() {
        this.currencyService = new CurrencyService(new Parser(), new CurrencyDAO(DynamoDBSource.getInstance()));
    }
    /*
    * Try to replace Map<String, Object> to APIGatewayV2HTTPEvent
    * For this one method it looks like an attempt parse ready object to map of object (json to map) (extractStringParams)
    * Need to do it in proper way
    * */
    @Override
    public APIGatewayV2HTTPResponse handleRequest(Map<String, Object> map, Context context) {
        Map<String, String> params = extractStringParams(map);
        if(params.containsKey(CURRENCY_PARAM)){
            try {
                String currencyName = params.get(CURRENCY_PARAM);
                Currency currency  =  currencyService.getCurrencyByName(currencyName);
                return buildResponse(SUCCESSFUL_STATUS_CODE, new ObjectMapper().writeValueAsString(currency));
            } catch (ResourceNotFoundException | JsonProcessingException e) {
                return buildResponse(NOT_FOUND_STATUS_CODE, NOT_FOUND_MESSAGE);
            }
        }
        return buildResponse(NOT_FOUND_STATUS_CODE, NOT_SPECIFY_CURRENCY_MESSAGE);
    }

    private Map<String, String> extractStringParams(Map<String, Object> map){
        return (Map<String, String>) map.getOrDefault(RAW_QUERY_STRING, new HashMap<>());
    }

    private APIGatewayV2HTTPResponse buildResponse(int code, String body){
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(code)
                .withBody(body)
                .withIsBase64Encoded(false)
                .withHeaders(Collections.singletonMap("Content-Type", "application/json"))
                .build();
    }

}
