package com.demo.aws.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.aws.dao.CustomerDAO;
import com.demo.aws.domain.Customer;
import com.demo.aws.exception.ObjectNotFoundException;

@RestController
@RequestMapping(value = "customers")
public class CustomerController {

    @Autowired
    private CustomerDAO dao;

    /**
     * create entity | JSON request : { "firstName": "Sovanndara", "lastName" : "Uom", "premium" : true }
     * 
     * @param customer
     * @return
     */
    @RequestMapping(value = "v1", method = RequestMethod.POST, produces = "application/json")
    public Customer createCustomer(@RequestBody Customer customer) {
        // return repository.save(customer);
        return dao.save(customer);
    }

    /**
     * find entity by id
     * 
     * @param id
     * @return
     * @throws ObjectNotFoundException
     */
    @RequestMapping(value = "v1/{id}", method = RequestMethod.GET)
    public Customer findById(@PathVariable String id) throws ObjectNotFoundException {
        // Customer domain = repository.findOne(id);
        Customer domain = dao.findById(id);
        if (domain == null) {
            throw new ObjectNotFoundException("Id is not found");
        }
        return domain;
    }

    /**
     * update entity with id
     * 
     * @param id
     * @param customer
     * @return
     * @throws ObjectNotFoundException
     */
    @RequestMapping(value = "v1/{id}", method = RequestMethod.PUT, produces = "application/json")
    public Customer update(@PathVariable String id, @RequestBody Customer customer) throws ObjectNotFoundException {
        Customer domain = dao.findById(id);
        if (domain == null) {
            throw new ObjectNotFoundException("Id is not found");
        }

        domain.setFirstName(customer.getFirstName());
        domain.setLastName(customer.getLastName());
        domain.setPremium(customer.getPremium());
        domain.setVersion(customer.getVersion());
        dao.update(domain);
        return domain;
    }

    /**
     * delete entity with id
     * 
     * @param id
     * @throws ObjectNotFoundException
     */
    @RequestMapping(value = "v1/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable String id) throws ObjectNotFoundException {
        Customer domain = dao.findById(id);
        if (domain == null) {
            throw new ObjectNotFoundException("Id is not found");
        }
        dao.delete(domain);
    }

    /**
     * search entity
     * 
     * @param name
     * @param premium
     * @return
     */
    @RequestMapping(value = "v1/search", method = RequestMethod.GET)
    public Iterable<Customer> customerSearch(@RequestParam(value = "premium", required = false) Boolean premium) {
        return dao.findByPremium(premium);
    }

    /**
     * search entity
     * 
     * @param name
     * @param premium
     * @return
     */
    @RequestMapping(value = "v1/all", method = RequestMethod.GET)
    public Collection<Customer> getAll() {
        return dao.getAll();
    }

}
