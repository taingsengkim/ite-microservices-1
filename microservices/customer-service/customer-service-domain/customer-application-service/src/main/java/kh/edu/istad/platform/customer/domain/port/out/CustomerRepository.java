package kh.edu.istad.platform.customer.domain.port.out;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface CustomerRepository {

//    Optional<Customer> save(Customer customer);
//
//    Optional<Customer> findById(CustomerId customerId);


    Mono<Customer> save(Customer customer);

    Mono<Customer> findById(CustomerId customerId);

    Flux<Customer> findAll();


    Mono<Void> deleteById(CustomerId customerId);

    Mono<Customer> updateCustomer(CustomerId customerId,UpdateCustomerCommand updateCustomerCommand);

    Mono<Customer> findByUsername(String username);

    Flux<Customer> query();

}
