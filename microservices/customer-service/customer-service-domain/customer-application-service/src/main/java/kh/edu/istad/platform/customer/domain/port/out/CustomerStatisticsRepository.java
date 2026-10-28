package kh.edu.istad.platform.customer.client.domain.port.out;

import kh.edu.istad.platform.customer.client.domain.dto.CustomerStatistics;
import reactor.core.publisher.Mono;

public interface CustomerStatisticsRepository {

    Mono<CustomerStatistics> getStatistics();
}