package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UpdateCustomerUseCase {
    private final CustomerRepository customerRepository;
    public Mono<Customer> updateCustomer(CustomerId customerId, UpdateCustomerCommand updateCustomerCommand){
        return customerRepository.findById(customerId)
                .switchIfEmpty(Mono.error(new CustomerDomainException("Customer not found with this id!")))
                .flatMap(customer -> customerRepository.updateCustomer(customerId,updateCustomerCommand));
    }
}
