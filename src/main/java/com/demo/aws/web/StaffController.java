package com.demo.aws.web;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.demo.aws.dao.StaffDao;
import com.demo.aws.domain.Staff;
import com.demo.aws.exception.ObjectNotFoundException;

@RestController
@RequestMapping(value = "staffs")
public class StaffController {

    @Autowired
    private StaffDao repository;

    /**
     * create entity JSON request : { "firstName": "Sok", "lastName" : "San", "gender" : "Male", "position" : "BE Developer",
     * "emailAddress" : "uom.sovanndara@gmail.com" }
     * 
     * @param staff
     * @return
     */
    @RequestMapping(value = "v1", method = RequestMethod.POST, produces = "application/json")
    public Staff create(@RequestBody Staff staff) {
        return repository.save(staff);
    }

    /**
     * find entity by id
     * 
     * @param id
     * @return
     * @throws ObjectNotFoundException
     */
    @RequestMapping(value = "v1/{id}", method = RequestMethod.GET)
    public Staff findById(@PathVariable String id) throws ObjectNotFoundException {
        Staff domain = repository.findById(id);
        if (domain == null) {
            throw new ObjectNotFoundException("Id is not found");
        }
        return domain;
    }

    /**
     * update entity with id
     * 
     * @param id
     * @param staff
     * @return
     * @throws ObjectNotFoundException
     */
    @RequestMapping(value = "v1/{id}", method = RequestMethod.PUT, produces = "application/json")
    public Staff update(@PathVariable String id, @RequestBody Staff staff) throws ObjectNotFoundException {

        Staff domain = repository.findById(id);
        if (domain == null) {
            throw new ObjectNotFoundException("Id is not found");
        }

        domain.setFirstName(staff.getFirstName());
        domain.setLastName(staff.getLastName());
        domain.setGender(staff.getGender());
        domain.setPosition(staff.getPosition());
        domain.setEmailAddress(staff.getEmailAddress());
        domain.setVersion(staff.getVersion());
        domain.setState(staff.getState());
        domain.setCreatedDate(staff.getCreatedDate());
        domain.setUpdatedDate(new Date());

        repository.update(domain);
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
        Staff domain = repository.findById(id);
        if (domain == null) {
            throw new ObjectNotFoundException("Id is not found");
        }
        repository.delete(domain);
    }
}
