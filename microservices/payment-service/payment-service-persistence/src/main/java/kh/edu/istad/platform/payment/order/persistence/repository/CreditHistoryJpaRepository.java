package kh.edu.istad.platform.payment.order.persistence.repository;

import kh.edu.istad.platform.payment.order.persistence.entity.CreditHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CreditHistoryJpaRepository extends JpaRepository<CreditHistoryEntity, UUID> {
    List<CreditHistoryEntity> findByCustomerId(UUID customerId);
}