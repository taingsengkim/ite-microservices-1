
package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.CustomerWithSegmentType;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetCustomerByIdUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerSegmentRepository customerSegmentRepository;

    public Mono<CustomerWithSegmentType> getCustomerById(CustomerId customerId) {
        return customerRepository.findById(customerId)
                .switchIfEmpty(Mono.error(new CustomerDomainException("Customer not found")))
                .flatMap(customer -> {
                    if (customer.getCustomerSegmentId() == null) {
                        return Mono.just(new CustomerWithSegmentType(customer, null));
                    }
                    return customerSegmentRepository.findById(customer.getCustomerSegmentId())
                            .map(segment -> new CustomerWithSegmentType(customer, segment.getType()))
                            .defaultIfEmpty(new CustomerWithSegmentType(customer, null));
                });
    }
}
