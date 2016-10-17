package org.fdm.boot;

// import java.util.List;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping("/customers")
public class CustomerRestController {

    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping(method=RequestMethod.GET)
    Iterable<Customer> findAll() {
        return customerRepository.findAll();
    }

    @RequestMapping(path="{id}", method=RequestMethod.GET)
    Customer findOne(@PathVariable Long id) {
        return customerRepository.findOne(id);
    }

    @RequestMapping(path="by_last_name/{lastName}", method=RequestMethod.GET)
    Collection<Customer> findByLastName(@PathVariable String lastName) {
        return customerRepository.findByLastName(lastName);
    }

    @RequestMapping(method=RequestMethod.POST)
    ResponseEntity<?> save(@RequestBody Customer customer){
        Customer result = customerRepository.save(customer);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(result.getId()).toUri());
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
    }

}
