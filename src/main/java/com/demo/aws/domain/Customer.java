package com.demo.aws.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;

/**
 * @DynamoDBTable annotation maps the Customer class to the customers table
 * @author dmiuser
 * @Ref : http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.Annotations.html
 */
@DynamoDBTable(tableName = "customers")
public class Customer {

    private String  id;
    private String  firstName;
    private String  lastName;
    private Boolean premium;
    private Long    version;

    /**
     * @DynamoDBHashKey annotation maps the Id property to the primary key.
     * @return
     */
    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "premiumIndex")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @DynamoDBAttribute annotation is added to each property to ensure that the property names match exactly with the tables
     *                    created in
     * @return
     */
    @DynamoDBAttribute
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @DynamoDBAttribute
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @DynamoDBAttribute
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "premiumIndex")
    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    /**
     * @DynamoDBVersionAttribute will be incremented by one. Additionally, if a version field is updated or a key generated, the
     *                           object passed in is updated as a result of the operation.
     * @return
     */
    @DynamoDBVersionAttribute
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
