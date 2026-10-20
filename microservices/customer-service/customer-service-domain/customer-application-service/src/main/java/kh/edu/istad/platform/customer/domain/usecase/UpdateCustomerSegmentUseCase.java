
package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerSegmentCommand;
import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateCustomerSegmentUseCase {

    private final CustomerSegmentRepository customerSegmentRepository;

    public Mono<CustomerSegment> execute(CustomerSegmentId id, UpdateCustomerSegmentCommand command) {
        return customerSegmentRepository.update(id, command);
    }
}
