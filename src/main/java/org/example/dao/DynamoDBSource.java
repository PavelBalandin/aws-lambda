package org.example.dao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class DynamoDBSource {
    private static Regions REGION;
    private static final String REGION_ENV_VARIABLE = "Region";

    private static final AmazonDynamoDB instance = initDynamoDbClient();

    private static AmazonDynamoDB initDynamoDbClient(){
        REGION = Regions.fromName(System.getenv(REGION_ENV_VARIABLE));
        return  AmazonDynamoDBClientBuilder.standard()
                .withRegion(REGION)
                .build();
    }

    public static AmazonDynamoDB getInstance() {
        return instance;
    }
}
