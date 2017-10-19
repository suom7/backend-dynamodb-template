package com.demo.aws.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.demo.aws.domain.Staff;
import com.demo.aws.exception.ObjectNotFoundException;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StaffDao {

    @Autowired
    private DynamoDB       client;

    @Autowired
    private AmazonDynamoDB dynamoDB;

    @Autowired
    private DynamoDBMapper mapper;

    /**
     * save
     * 
     * @param domain
     * @return
     */
    public Staff save(Staff domain) {

        domain.setCreatedDate(new Date());
        domain.setState(true);

        mapper.save(domain);
        return domain;
    }

    /**
     * save all
     * 
     * @param domain
     * @return
     */
    public void save(List<Staff> domain) {
        mapper.batchSave(domain);
    }

    /**
     * find by id
     * 
     * @param id
     * @return
     */
    public Staff findById(String id) {
        return mapper.load(Staff.class, id);
    }

    /**
     * update
     * 
     * @param domain
     * @return
     * @throws ObjectNotFoundException
     */
    public Staff update(Staff domain) throws ObjectNotFoundException {
        Staff existingDomain = findById(domain.getId());
        if (existingDomain == null) {
            throw new ObjectNotFoundException("Object is not found");
        }
        mapper.save(domain);
        return domain;
    }

    /**
     * delete
     * 
     * @param domain
     */
    public void delete(Staff domain) {
        mapper.delete(domain);
    }

}
