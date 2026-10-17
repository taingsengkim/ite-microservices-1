package kh.edu.istad.platform.business.domain.port.out;
import kh.edu.istad.platform.business.domain.entity.Business;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface BusinessRepository {
    Mono<Business> save(Business business);
}