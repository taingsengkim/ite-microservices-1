
package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeleteCustomerUseCase {

    private final CustomerRepository customerRepository;

    public Mono<Void> deleteCustomer(CustomerId customerId) {
        return customerRepository.findById(customerId)
                .switchIfEmpty(Mono.error(new CustomerDomainException("Customer not found")))
                .then(customerRepository.deleteById(customerId));
    }
}
