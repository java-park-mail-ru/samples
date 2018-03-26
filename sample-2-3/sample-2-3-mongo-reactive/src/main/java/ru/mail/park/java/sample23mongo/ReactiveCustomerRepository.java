package ru.mail.park.java.sample23mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveCustomerRepository extends ReactiveMongoRepository<Customer, String> {

	Mono<Customer> findByFirstName(String firstName);

	Flux<Customer> findByLastName(String lastName);

}