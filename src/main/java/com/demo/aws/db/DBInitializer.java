package com.demo.aws.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.demo.aws.domain.Customer;
import com.demo.aws.domain.Staff;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DBInitializer {

    /**
     * The AWS SDK for Java provides a DynamoDBMapper class, allowing you to map your client-side classes to DynamoDB tables. To
     * use DynamoDBMapper, you define the relationship between items in a DynamoDB table and their corresponding object instances
     * in your code. The DynamoDBMapper class enables you to access your tables, perform various create, read, update and delete
     * (CRUD) operations, and execute queries.
     * 
     * Note The DynamoDBMapper class does not allow you to create, update, or delete tables. To perform those tasks, use the
     * low-level SDK for Java interface instead. For more information, see Working with Tables: Java.
     */
    private DynamoDBMapper mapper;
    private DynamoDB       client;

    @Autowired
    public DBInitializer(DynamoDBMapper mapper, DynamoDB client) {
        this.mapper = mapper;
        this.client = client;
    }

    @PostConstruct
    public void init() throws InterruptedException {
        // Uncomment to use the low level api
        // createCustomersTable();

        // use DynamoDBMapper
        createCustomersTableWithMapper();
        createStaffTableWithMapper();
    }

    private void createCustomersTable() throws InterruptedException {

        List<AttributeDefinition> attributeDefinitions = new ArrayList<>(1);
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("id").withAttributeType(ScalarAttributeType.S));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("premium").withAttributeType(ScalarAttributeType.N));

        List<KeySchemaElement> keyDefinitions = new ArrayList<>(2);
        keyDefinitions.add(new KeySchemaElement().withAttributeName("id").withKeyType(KeyType.HASH));

        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(1L, 1L);

        GlobalSecondaryIndex globalSecondaryIndex = new GlobalSecondaryIndex().withIndexName("premiumIndex")
                .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
                .withKeySchema(new KeySchemaElement("premium", KeyType.HASH), new KeySchemaElement("id", KeyType.RANGE))
                .withProvisionedThroughput(provisionedThroughput);

        CreateTableRequest request = new CreateTableRequest().withTableName("customers").withKeySchema(keyDefinitions)
                .withAttributeDefinitions(attributeDefinitions).withProvisionedThroughput(provisionedThroughput)
                .withGlobalSecondaryIndexes(globalSecondaryIndex);

        try {
            Table table = client.createTable(request);
            table.waitForActive();
        }
        catch (ResourceInUseException e) {
            log.info("Table {} already exists", request.getTableName());
        }
    }

    private void createCustomersTableWithMapper() throws InterruptedException {
        // map with java object
        CreateTableRequest request = mapper.generateCreateTableRequest(Customer.class);

        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(1L, 1L);
        request.setProvisionedThroughput(provisionedThroughput);
        request.getGlobalSecondaryIndexes().forEach(index -> {
            index.setProvisionedThroughput(provisionedThroughput);
            index.setProjection(new Projection().withProjectionType(ProjectionType.ALL));
        });

        try {
            Table table = client.createTable(request);
            table.waitForActive();
        }
        catch (ResourceInUseException e) {
            log.info("Table {} already exists", request.getTableName());
        }
    }

    private void createStaffTableWithMapper() throws InterruptedException {
        // map with java object
        CreateTableRequest request = mapper.generateCreateTableRequest(Staff.class);
        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(1L, 1L);
        request.setProvisionedThroughput(provisionedThroughput);
        request.getGlobalSecondaryIndexes().forEach(index -> {
            index.setProvisionedThroughput(provisionedThroughput);
            index.setProjection(new Projection().withProjectionType(ProjectionType.ALL));
        });

        try {
            Table table = client.createTable(request);
            table.waitForActive();
        }
        catch (ResourceInUseException e) {
            log.info("Table {} already exists", request.getTableName());
        }
    }
}
