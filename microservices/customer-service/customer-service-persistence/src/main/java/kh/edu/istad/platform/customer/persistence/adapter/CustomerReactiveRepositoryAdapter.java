package kh.edu.istad.platform.customer.persistence.adapter;


import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.persistence.filter.FilterCondition;
import kh.edu.istad.common.persistence.filter.FilterOperator;
import kh.edu.istad.common.persistence.filter.GenericMongoQueryBuilder;
import kh.edu.istad.platform.customer.domain.dto.CustomerFilter;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.persistence.entity.CustomerDocument;
import kh.edu.istad.platform.customer.persistence.entity.CustomerReadDocument;
import kh.edu.istad.platform.customer.persistence.mapper.CustomerPersistenceMapper;
import kh.edu.istad.platform.customer.persistence.query.CustomerQueryBuilder;
import kh.edu.istad.platform.customer.persistence.repository.CustomerMongoRepository;
import kh.edu.istad.platform.customer.persistence.repository.CustomerSegmentMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CustomerReactiveRepositoryAdapter implements CustomerRepository {

    private final CustomerMongoRepository customerMongoRepository;
    private final CustomerPersistenceMapper customerPersistenceMapper;
    private final CustomerSegmentMongoRepository customerSegmentMongoRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Mono<Customer> save(Customer customer) {
        if (customer.getCustomerSegmentId() == null) {
            return Mono.error(new CustomerDomainException("Customer segment ID is required"));
        }

        return customerSegmentMongoRepository
                .findById(customer.getCustomerSegmentId().value().toString())
                .switchIfEmpty(Mono.error(new CustomerDomainException("Customer segment not found")))
                .flatMap(segmentDocument -> {
                    CustomerDocument document = customerPersistenceMapper.toDocument(customer);
                    document.setCustomerSegmentDocument(segmentDocument);
                    return customerMongoRepository.save(document).thenReturn(customer);
                });
    }

//    @Override
//    public Mono<Customer> findById(CustomerId customerId) {
//        return customerMongoRepository.findById(customerId.value().toString()).map(customerPersistenceMapper::toDomain);
//    }
    @Override
    public Mono<Customer> findById(CustomerId customerId) {
        Query query = Query.query(
                Criteria.where("_id").is(customerId.value().toString())
        );

        return reactiveMongoTemplate
                .findOne(query, CustomerReadDocument.class)
                .map(customerPersistenceMapper::toDomain);
    }

//    @Override
//    public Flux<Customer> findAll() {
//        return customerMongoRepository.findAll().map(d->customerPersistenceMapper.toDomain(d));
//    }
    @Override
    public Flux<Customer> findAll() {
        return reactiveMongoTemplate.findAll(CustomerReadDocument.class).map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(CustomerId customerId) {
        return customerMongoRepository.deleteById(customerId.value().toString());
    }


    @Override
    public Mono<Customer> updateCustomer(CustomerId customerId, UpdateCustomerCommand command) {
        Query query = Query.query(Criteria.where("_id").is(customerId.value().toString()));
        Update update = new Update();

        if (command.familyName() != null) update.set("familyName", command.familyName());
        if (command.givenName() != null) update.set("givenName", command.givenName());
        if (command.email() != null) update.set("email", command.email());
        if (command.phoneNumber() != null) update.set("phoneNumber", command.phoneNumber());

        Mono<Void> updateSegment = Mono.empty();

        if (command.customerSegmentId() != null) {
            updateSegment = customerSegmentMongoRepository.findById(command.customerSegmentId().toString())
                    .switchIfEmpty(Mono.error(new CustomerDomainException("Customer segment not found")))
                    .flatMap(segment -> {
                        update.set("customerSegmentDocument", segment);
                        return Mono.empty();
                    });
        }

        if (update.getUpdateObject().isEmpty() && command.customerSegmentId() == null) {
            return findById(customerId)
                    .switchIfEmpty(Mono.error(new CustomerDomainException("Customer not found")));
        }

        return updateSegment.then(reactiveMongoTemplate.updateFirst(query, update, CustomerDocument.class))
                .flatMap(result -> {
                    if (result.getMatchedCount() == 0) {
                        return Mono.error(new CustomerDomainException("Customer not found"));
                    }
                    return findById(customerId);
                });
    }


    @Override
    public Mono<Customer> findByUsername(String username) {
        Query query = Query.query(
                Criteria.where("username").is(username)
        );
        return reactiveMongoTemplate.findOne(query, CustomerReadDocument.class).map(customerPersistenceMapper::toDomain);
    }


    @Override
    public Flux<Customer> query(CustomerFilter filter) {
        List<FilterCondition> conditions = new ArrayList<>();

        if (filter.username() != null && !filter.username().isBlank()) {
            conditions.add(new FilterCondition("username", FilterOperator.CONTAINS, filter.username().trim()));
        }

        if (filter.familyName() != null && !filter.familyName().isBlank()) {
            conditions.add(new FilterCondition("familyName", FilterOperator.CONTAINS, filter.familyName().trim()));
        }

        if (filter.givenName() != null && !filter.givenName().isBlank()) {
            conditions.add(new FilterCondition("givenName", FilterOperator.CONTAINS, filter.givenName().trim()));
        }

        if (filter.email() != null && !filter.email().isBlank()) {
            conditions.add(new FilterCondition("email", FilterOperator.CONTAINS, filter.email().trim()));
        }

        if (filter.phoneNumber() != null && !filter.phoneNumber().isBlank()) {
            conditions.add(new FilterCondition("phoneNumber", FilterOperator.CONTAINS, filter.phoneNumber().trim()));
        }

        if (filter.customerStatus() != null && !filter.customerStatus().isBlank()) {
            conditions.add(new FilterCondition("status", FilterOperator.EQUALS, filter.customerStatus().trim()));
        }

        Query query = GenericMongoQueryBuilder.build(conditions);

        return reactiveMongoTemplate.find(query, CustomerReadDocument.class, "customers")
                .map(customerPersistenceMapper::toDomain);
    }



}
