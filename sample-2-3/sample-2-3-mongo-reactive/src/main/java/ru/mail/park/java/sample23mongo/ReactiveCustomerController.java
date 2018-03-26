package ru.mail.park.java.sample23mongo;

import com.mongodb.MongoWriteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customer")
public class ReactiveCustomerController {

    private final ReactiveCustomerRepository customerRepository;

    public ReactiveCustomerController(ReactiveCustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping
    Mono<String> save(@RequestBody Customer customer) {
        return customerRepository.save(customer).map(Customer::getId);
    }

    @ExceptionHandler(MongoWriteException.class)
    ResponseEntity<String> writeFailed(MongoWriteException mwe) {
        if (mwe.getCode() == 11000) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Customer already exists");
        } else {
            throw mwe;
        }
    }

    @GetMapping
    Flux<Customer> getAll() {
        return customerRepository.findAll();
    }

    @GetMapping("/name/{firstName}")
    Mono<ResponseEntity<Customer>> getByFirstName(@PathVariable String firstName) {
        return customerRepository.findByFirstName(firstName)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{lastName}")
    Flux<Customer> getByLastName(@PathVariable String lastName) {
        return customerRepository.findByLastName(lastName);
    }
}
