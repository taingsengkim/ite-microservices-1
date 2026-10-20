package kh.edu.istad.platform.customer.persistence.repository;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.persistence.adapter.CustomerReactiveRepositoryAdapter;
import kh.edu.istad.platform.customer.persistence.entity.CustomerDocument;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerMongoRepository  extends ReactiveMongoRepository<CustomerDocument, String> {

    @Query("${username:  {$eq:  ?0 }}")
    Mono<CustomerDocument> findByUsername(String username);

}
