package kh.edu.istad.platform.customer.persistence.repository;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.persistence.adapter.CustomerReactiveRepositoryAdapter;
import kh.edu.istad.platform.customer.persistence.entity.CustomerDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMongoRepository  extends ReactiveMongoRepository<CustomerDocument, String> {

}
