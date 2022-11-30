package org.example.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SystemStubsExtension.class)
class DynamoDBSourceTest {

    @SystemStub
    EnvironmentVariables environmentVariables;

    private static final String REGION_ENV_NAME = "Region";
    private static final String REGION_ENV_VALUE = "eu-central-1";

    @BeforeEach
    void setUp(){
        environmentVariables.set(REGION_ENV_NAME, REGION_ENV_VALUE);
    }

    @Test
    void getInstanceShouldReturnSameAmazonDynamoDBInstance() {
        AmazonDynamoDB amazonDynamoDBFirst = DynamoDBSource.getInstance();
        AmazonDynamoDB amazonDynamoDBSecond = DynamoDBSource.getInstance();

        assertEquals(amazonDynamoDBFirst, amazonDynamoDBSecond);
    }
}