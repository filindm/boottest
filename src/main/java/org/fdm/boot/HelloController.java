package org.fdm.boot;

import java.util.List;

// import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class HelloController {

    // @Inject
    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping("/")
    public String index() {
        return "Customer repo: " + customerRepository;
    }

    @RequestMapping(path="/customer/{id}", method=RequestMethod.GET)
    public Customer getCustomer(@PathVariable Long id) {
        return customerRepository.findOne(id);
    }

    @RequestMapping(path="/customersByLastName/{lastName}", method=RequestMethod.GET)
    public List<Customer> getCustomersByLastName(@PathVariable String lastName) {
        return customerRepository.findByLastName(lastName);
    }

}
