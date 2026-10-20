package kh.edu.istad.platform.customer.domain.port.out;

import kh.edu.istad.platform.customer.domain.dto.CustomerStatistics;
import reactor.core.publisher.Mono;

public interface CustomerStatisticsRepository {

    Mono<CustomerStatistics> getStatistics();
}