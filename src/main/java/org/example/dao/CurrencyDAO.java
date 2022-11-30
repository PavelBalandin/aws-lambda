package org.example.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import org.example.domain.Currency;

import java.util.HashMap;
import java.util.Map;

public class CurrencyDAO {

    private static final String DYNAMODB_TABLE_NAME = "Currencies";

    private final AmazonDynamoDB amazonDynamoDB;

    public CurrencyDAO(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
    }

    public Currency getCurrencyByName(String name){
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        Table table = dynamoDB.getTable(DYNAMODB_TABLE_NAME);
        Item item = table.getItem("currency", name);

        return new Currency((String) item.get("name"), (String) item.get("price"));
    }

    public void saveCurrency(Currency currency) throws ConditionalCheckFailedException {
        Map<String, AttributeValue> attributesMap = new HashMap<>();

        attributesMap.put("name", new AttributeValue(String.valueOf(currency.getName())));
        attributesMap.put("price", new AttributeValue(String.valueOf(currency.getPrice())));

        amazonDynamoDB.putItem(DYNAMODB_TABLE_NAME, attributesMap);
    }

}
