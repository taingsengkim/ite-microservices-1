package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.CustomerSegmentFilter;
import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class QueryCustomerSegmentUseCase {

    private final CustomerSegmentRepository customerSegmentRepository;

    public Flux<CustomerSegment> execute(CustomerSegmentFilter filter) {
        return customerSegmentRepository.query(filter);
    }
}