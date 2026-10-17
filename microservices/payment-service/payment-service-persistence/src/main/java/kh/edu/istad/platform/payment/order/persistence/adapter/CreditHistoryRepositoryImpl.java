package kh.edu.istad.platform.payment.order.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.CreditHistoryId;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.platform.payment.order.entity.CreditHistory;
import kh.edu.istad.platform.payment.order.persistence.entity.CreditHistoryEntity;
import kh.edu.istad.platform.payment.order.persistence.repository.CreditHistoryJpaRepository;
import kh.edu.istad.platform.payment.order.port.out.CreditHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CreditHistoryRepositoryImpl implements CreditHistoryRepository {

    private final CreditHistoryJpaRepository creditHistoryJpaRepository;

    @Override
    public CreditHistory save(CreditHistory creditHistory) {
        creditHistoryJpaRepository.save(CreditHistoryEntity.builder()
                .id(creditHistory.getId().value())
                .customerId(creditHistory.getCustomerId().value())
                .amount(creditHistory.getAmount().amount())
                .transactionType(creditHistory.getTransactionType())
                .build());
        return creditHistory;
    }

    @Override
    public Optional<List<CreditHistory>> findByCustomerId(CustomerId customerId) {
        List<CreditHistoryEntity> entities = creditHistoryJpaRepository.findByCustomerId(customerId.value());
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(entities.stream()
                .map(entity -> CreditHistory.Builder.builder()
                        .id(new CreditHistoryId(entity.getId()))
                        .customerId(new CustomerId(entity.getCustomerId()))
                        .amount(new Money(entity.getAmount()))
                        .transactionType(entity.getTransactionType())
                        .build())
                .collect(Collectors.toList()));
    }
}

