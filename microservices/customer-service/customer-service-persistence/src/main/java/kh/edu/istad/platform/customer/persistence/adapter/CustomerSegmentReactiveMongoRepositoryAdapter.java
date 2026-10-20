package kh.edu.istad.platform.customer.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.CustomerSegmentId;
import kh.edu.istad.common.persistence.filter.FilterCondition;
import kh.edu.istad.common.persistence.filter.FilterOperator;
import kh.edu.istad.common.persistence.filter.GenericMongoQueryBuilder;
import kh.edu.istad.platform.customer.domain.dto.CustomerSegmentFilter;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerSegmentCommand;
import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentRepository;
import kh.edu.istad.platform.customer.persistence.entity.CustomerSegmentDocument;
import kh.edu.istad.platform.customer.persistence.mapper.CustomerSegmentPersistenceMapper;
import kh.edu.istad.platform.customer.persistence.repository.CustomerSegmentMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomerSegmentReactiveMongoRepositoryAdapter implements CustomerSegmentRepository {

    private final CustomerSegmentMongoRepository customerSegmentMongoRepository;
    private final CustomerSegmentPersistenceMapper customerSegmentPersistenceMapper;
    private final ReactiveMongoTemplate mongoTemplate;

    public Flux<CustomerSegment> query(CustomerSegmentFilter filter) {
        List<FilterCondition> conditions = new ArrayList<>();

        if (filter.type() != null && !filter.type().isBlank()) {
            conditions.add(new FilterCondition("type", FilterOperator.CONTAINS, filter.type().trim()));
        }

        Query query = GenericMongoQueryBuilder.build(conditions);

        return mongoTemplate.find(query, CustomerSegmentDocument.class, "customer_segments")
                .map(customerSegmentPersistenceMapper::toDomain);
    }

    @Override
    public Mono<CustomerSegment> save(CustomerSegment customerSegment) {
        return customerSegmentMongoRepository.save(customerSegmentPersistenceMapper.toDocument(customerSegment)).map(customerSegmentPersistenceMapper::toDomain);
    }

    @Override
    public Mono<CustomerSegment> findById(CustomerSegmentId customerSegmentId) {
        return customerSegmentMongoRepository
                .findById(customerSegmentId.value().toString()).map(customerSegmentPersistenceMapper::toDomain);
    }

    @Override
    public Flux<CustomerSegment> getAll() {
        return customerSegmentMongoRepository.findAll().map(customerSegmentPersistenceMapper::toDomain);
    }

    @Override
    public Mono<CustomerSegment> update(CustomerSegmentId customerSegmentId, UpdateCustomerSegmentCommand updateCustomerSegmentCommand) {
        return customerSegmentMongoRepository.findById(customerSegmentId.value().toString())
                .switchIfEmpty(Mono.error(new CustomerDomainException("Customer segment not found!")))
                .map(customerSegmentPersistenceMapper::toDomain)
                .flatMap(customerSegment -> {
                    customerSegment.updateType(updateCustomerSegmentCommand.type());
                    return customerSegmentMongoRepository.save(customerSegmentPersistenceMapper.toDocument(customerSegment)).map(customerSegmentPersistenceMapper::toDomain);
                });
    }

    @Override
    public Mono<Void> delete(CustomerSegmentId customerSegmentId) {
        return customerSegmentMongoRepository.findById(customerSegmentId.value().toString())
                .switchIfEmpty(Mono.error(new CustomerDomainException("Customer segment id not found!")))
                .flatMap(segment->customerSegmentMongoRepository.deleteById(segment.getId()));
    }
}
