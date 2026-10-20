package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.CustomerStatistics;
import kh.edu.istad.platform.customer.domain.port.out.CustomerStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetCustomerStatisticsUseCase {

    private final CustomerStatisticsRepository statisticsRepository;

    public Mono<CustomerStatistics> execute() {
        return statisticsRepository.getStatistics();
    }
}