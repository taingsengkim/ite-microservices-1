package kh.edu.istad.platform.customer.client.domain.usecase;

import kh.edu.istad.platform.customer.client.domain.dto.CustomerFilter;
import kh.edu.istad.platform.customer.client.domain.dto.CustomerWithSegmentType;
import kh.edu.istad.platform.customer.client.domain.entity.Customer;
import kh.edu.istad.platform.customer.client.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.client.domain.port.out.CustomerSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@RequiredArgsConstructor
//@Service
//public class QueryCustomerUseCase {
//    private final CustomerRepository customerRepository;
//
//    public Flux<Customer> findAll(){
//        return customerRepository.findAll();
//    }
//    public Flux<Customer> query(CustomerFilter customerFilter){
//        return customerRepository.query(customerFilter);
//    }
//}



@RequiredArgsConstructor
@Service
public class QueryCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerSegmentRepository customerSegmentRepository;

    public Flux<CustomerWithSegmentType> findAll() {
        return enrichWithSegmentType(customerRepository.findAll());
    }

    public Flux<CustomerWithSegmentType> query(CustomerFilter customerFilter) {
        return enrichWithSegmentType(customerRepository.query(customerFilter));
    }

    private Flux<CustomerWithSegmentType> enrichWithSegmentType(Flux<Customer> customers) {

        return customers.flatMap(customer -> {
            if (customer.getCustomerSegmentId() == null) {
                return Mono.just(new CustomerWithSegmentType(customer, null));
            }
            return customerSegmentRepository
                    .findById(customer.getCustomerSegmentId())
                    .map(segment -> new CustomerWithSegmentType(customer, segment.getType()
                    ))
                    .switchIfEmpty(Mono.just(new CustomerWithSegmentType(customer, null)));
        });
    }
}