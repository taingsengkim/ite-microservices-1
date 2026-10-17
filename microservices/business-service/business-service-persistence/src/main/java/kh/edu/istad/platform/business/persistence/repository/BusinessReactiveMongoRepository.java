package kh.edu.istad.platform.business.persistence.repository;

import kh.edu.istad.platform.business.persistence.document.BusinessMongoDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BusinessReactiveMongoRepository extends ReactiveMongoRepository<BusinessMongoDocument, UUID> {
}