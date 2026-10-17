package kh.edu.istad.platform.business.persistence.adapter;

import kh.edu.istad.platform.business.domain.entity.Business;
import kh.edu.istad.platform.business.domain.port.out.BusinessRepository;
import kh.edu.istad.platform.business.persistence.document.BusinessMongoDocument;
import kh.edu.istad.platform.business.persistence.mapper.BusinessPersistenceMapper;
import kh.edu.istad.platform.business.persistence.repository.BusinessReactiveMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessRepositoryImpl implements BusinessRepository {

    private final BusinessReactiveMongoRepository mongoRepository;
    private final BusinessPersistenceMapper mapper;

    @Override
    public Mono<Business> save(Business business) {
        log.info("Persisting business document into MongoDB with id: {}", business.getId().value());

        return Mono.just(business)
                .map(mapper::businessToDocument)
                .flatMap(mongoRepository::save)
                .map(mapper::documentToBusiness);
    }
}