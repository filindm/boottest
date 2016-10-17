package org.fdm.boot;

// import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

    // @Inject
    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping("/")
    public String index() {
        return "Customer repo: " + customerRepository;
    }

}
