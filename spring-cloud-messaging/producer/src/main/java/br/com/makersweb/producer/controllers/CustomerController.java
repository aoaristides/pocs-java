package br.com.makersweb.producer.controllers;

import br.com.makersweb.producer.domain.Customer;
import br.com.makersweb.producer.services.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aaristides
 */
@RestController
@RequestMapping("/customers")
@Slf4j
public class CustomerController {

    private final MessageSender messageSender;

    public CustomerController(@Autowired MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @PostMapping
    public void create(@RequestBody Customer customer) {
        log.info("Create customer by name - {}", customer.name());
        messageSender.send(customer);
    }
}
