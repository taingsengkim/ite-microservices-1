package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeleteCustomerSegmentUseCase {
    private final CustomerSegmentRepository customerSegmentRepository;
    public Mono<Void> execute(CustomerSegmentId customerSegmentId){
       return customerSegmentRepository.delete(customerSegmentId);
    }
}
