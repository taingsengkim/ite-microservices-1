package kh.edu.istad.platform.customer.client.persistence.repository;

import kh.edu.istad.platform.customer.client.persistence.entity.CustomerSegmentDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerSegmentMongoRepository extends ReactiveMongoRepository<CustomerSegmentDocument,String> {

}
