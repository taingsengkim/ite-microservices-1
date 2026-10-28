package kh.edu.istad.platform.customer.client.persistence.adapter;


import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.client.domain.dto.CustomerFilter;
import kh.edu.istad.platform.customer.client.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.client.domain.entity.Customer;
import kh.edu.istad.platform.customer.client.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.client.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.client.persistence.entity.CustomerDocument;
import kh.edu.istad.platform.customer.client.persistence.entity.CustomerReadDocument;
import kh.edu.istad.platform.customer.client.persistence.mapper.CustomerPersistenceMapper;
import kh.edu.istad.platform.customer.client.persistence.repository.CustomerMongoRepository;
import kh.edu.istad.platform.customer.client.persistence.repository.CustomerSegmentMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<Customer> updateCustomer(CustomerId customerId,UpdateCustomerCommand updateCustomerCommand) {
        return null;
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
        Query query = new Query();

        if (filter.username() != null && !filter.username().isBlank()) {
            query.addCriteria(Criteria.where("username").is(filter.username()));
        }

        if (filter.familyName() != null && !filter.familyName().isBlank()) {
            query.addCriteria(Criteria.where("familyName").is(filter.familyName()));
        }

        if (filter.givenName() != null && !filter.givenName().isBlank()) {
            query.addCriteria(Criteria.where("givenName").is(filter.givenName()));
        }

        if (filter.email() != null && !filter.email().isBlank()) {
            query.addCriteria(Criteria.where("email").is(filter.email()));
        }

        if (filter.phoneNumber() != null && !filter.phoneNumber().isBlank()) {
            query.addCriteria(Criteria.where("phoneNumber").is(filter.phoneNumber()));
        }

        if (filter.customerStatus() != null && !filter.customerStatus().isBlank()) {
            query.addCriteria(Criteria.where("customerStatus").is(filter.customerStatus()));
        }

//        return reactiveMongoTemplate
//                .find(query, CustomerDocument.class)
//                .map(customerPersistenceMapper::toDomain);
        return reactiveMongoTemplate
                .find(query, CustomerReadDocument.class)
                .map(customerPersistenceMapper::toDomain);
    }



}
