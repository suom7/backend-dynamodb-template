package com.demo.aws.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.demo.aws.domain.Customer;
import com.demo.aws.exception.ObjectNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDAO {

    @Autowired
    private DynamoDB       client;

    @Autowired
    private AmazonDynamoDB dynamoDB;

    /**
     * @Ref http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.html
     */
    @Autowired
    private DynamoDBMapper mapper;

    /**
     * update
     * 
     * @param domain
     * @return
     * @throws ObjectNotFoundException
     */
    public Customer update(Customer domain) throws ObjectNotFoundException {
        Customer existingDomain = findById(domain.getId());
        if (existingDomain == null) {
            throw new ObjectNotFoundException("Object is not found");
        }
        mapper.save(domain);
        return domain;
    }

    /**
     * save
     * 
     * @param domain
     * @return
     */
    public Customer save(Customer domain) {
        mapper.save(domain);
        return domain;
    }

    /**
     * save all
     * 
     * @param domain
     * @return
     */
    public void save(List<Customer> domain) {
        mapper.batchSave(domain);
    }

    /**
     * find by id
     * 
     * @param id
     * @return
     */
    public Customer findById(String id) {
        return mapper.load(Customer.class, id);
    }

    /**
     * delete
     * 
     * @param domain
     */
    public void delete(Customer domain) {
        mapper.delete(domain);
    }

    /**
     * count
     * 
     * @param id
     * @return
     */
    public Integer count() {
        DynamoDBQueryExpression<Customer> queryExpression = new DynamoDBQueryExpression<Customer>()
                .withHashKeyValues(new Customer());
        return mapper.count(Customer.class, queryExpression);
    }

    /**
     * @Ref Use Query http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/QueryingJavaDocumentAPI.html
     * @return
     */
    public List<Customer> findByPremium(Boolean premium) {
        QuerySpec spec = new QuerySpec();

        spec.withKeyConditionExpression("premium = :val").withValueMap(new ValueMap().withNumber(":val", premium ? 1 : 0));

        ItemCollection<QueryOutcome> customers = client.getTable("customers").getIndex("premiumIndex").query(spec);

        for(Item item : customers) {
            String json = item.toJSONPretty();
            System.out.println(json + ",");
        }
        return null;
    }

    /**
     * @Ref Use item http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/WorkingWithItems.html
     * @param customer
     * @return
     */
    public Customer saveWithItemApi(Customer customer) {
        customer.setId(UUID.randomUUID().toString());
        client.getTable("customers").putItem(new Item().withPrimaryKey("id", customer.getId())
                .with("firstName", customer.getFirstName()).with("lastName", customer.getLastName()));
        return customer;
    }

    /**
     * @Ref Use scan : http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Scan.html
     * @param name
     * @return
     */
    public List<Customer> findFirstName(String name) {
        Map<String, AttributeValue> lastKeyEvaluated;
        do {
            Map<String, AttributeValue> values = new HashMap<>();
            values.put(":val", new AttributeValue().withS(name));
            ScanRequest scanRequest = new ScanRequest().withLimit(100).withTableName("customers")
                    .withExpressionAttributeValues(values).withFilterExpression("firstName = :val")
                    .withProjectionExpression("id");

            ScanResult scanResult = dynamoDB.scan(scanRequest);

            scanResult.getItems().stream().forEach(System.out::println);

            lastKeyEvaluated = scanResult.getLastEvaluatedKey();
        }
        while (null != lastKeyEvaluated);

        return null;
    }

    public List<Customer> getAll() {

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":value", new AttributeValue().withN("1"));
        DynamoDBQueryExpression<Customer> queryExpression = new DynamoDBQueryExpression<Customer>()
                .withKeyConditionExpression("premium= :value").withExpressionAttributeValues(eav);
        List<Customer> result = mapper.query(Customer.class, queryExpression);

        return result;
    }

}
