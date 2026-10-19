package kh.edu.istad.platform.customer.persistence.adapter;


import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.persistence.entity.CustomerDocument;
import kh.edu.istad.platform.customer.persistence.mapper.CustomerPersistenceMapper;
import kh.edu.istad.platform.customer.persistence.repository.CustomerMongoRepository;
import kh.edu.istad.platform.customer.persistence.repository.CustomerSegmentMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        return customerSegmentMongoRepository
                .findById(customer.getCustomerSegmentId().value().toString())
                .switchIfEmpty(Mono.error(
                        new CustomerDomainException("Customer segment not found")
                ))
                .flatMap(segmentDocument -> {
                    CustomerDocument document =
                            customerPersistenceMapper.toDocument(customer);

                    // Set the existing segment document as the DBRef.
                    document.setCustomerSegmentDocument(segmentDocument);

                    return customerMongoRepository.save(document);
                })
                .map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Mono<Customer> findById(CustomerId customerId) {
        return customerMongoRepository.findById(customerId.value().toString()).map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Flux<Customer> findAll() {
        return customerMongoRepository.findAll().map(d->customerPersistenceMapper.toDomain(d));
    }

    @Override
    public Mono<Void> deleteById(CustomerId customerId) {
        return customerMongoRepository.deleteById(customerId.value().toString());
    }

    @Override
    public Mono<Customer> updateCustomer(CustomerId customerId,UpdateCustomerCommand updateCustomerCommand) {
        return null;
    }

    @Override
    public Mono<Customer> findByUsername(String username) {

        return customerMongoRepository.findByUsername(username).map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Flux<Customer> query() {
        Query query = Query.query(
                Criteria.where("username").eq("dw")
        );
        return  reactiveMongoTemplate.find(query, CustomerDocument.class).map(customerPersistenceMapper::toDomain);
    }



}
