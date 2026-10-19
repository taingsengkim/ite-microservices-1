package kh.edu.istad.platform.customer.persistence.adapter;


import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.persistence.mapper.CustomerPersistenceMapper;
import kh.edu.istad.platform.customer.persistence.repository.CustomerMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CustomerReactiveRepositoryAdapter implements CustomerRepository {

    private final CustomerMongoRepository customerMongoRepository;
    private final CustomerPersistenceMapper customerPersistenceMapper;

    @Override
    public Mono<Customer> save(Customer customer) {
        return customerMongoRepository
                .save(customerPersistenceMapper.toDocument(customer))
                .map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Mono<Customer> findById(CustomerId customerId) {
        return null;
    }

    @Override
    public Flux<Customer> findAll() {
        return customerMongoRepository.findAll().map(d->customerPersistenceMapper.toDomain(d));
    }
}
