package kh.edu.istad.platform.customer.client.domain.usecase;


import kh.edu.istad.platform.customer.client.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.client.domain.port.out.CustomerSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class    CreateCustomerSegmentUseCase {
    private final CustomerSegmentRepository customerSegmentRepository;
    public Mono<CustomerSegment> execute(CustomerSegment customerSegment){
        customerSegment.initiateCustomerSegment();
        return customerSegmentRepository.save(customerSegment);
    }
}
