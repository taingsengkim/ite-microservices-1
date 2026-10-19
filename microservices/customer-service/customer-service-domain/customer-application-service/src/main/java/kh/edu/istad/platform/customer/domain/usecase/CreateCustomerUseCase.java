package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.InitiatedCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.mapper.CustomerDomainMapper;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CreateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerSegmentRepository customerSegmentRepository;
    private final CustomerDomainMapper customerDomainMapper;

    public Mono<InitiatedCustomerResult> create(Customer customer) {
        customer.initiateCustomer();

        return customerSegmentRepository
                .findById(customer.getCustomerSegmentId())
                .switchIfEmpty(Mono.error(new CustomerDomainException("Customer segment not found")))
                .flatMap(segment -> customerRepository.save(customer).map(savedCustomer ->
                                customerDomainMapper.fromCustomerToInitiatedCustomerResult(savedCustomer, segment.getType())
                        )
                );
    }
}