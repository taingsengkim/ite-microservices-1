package kh.edu.istad.platform.payment.order.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.CreditEntryId;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.platform.payment.order.entity.CreditEntry;
import kh.edu.istad.platform.payment.order.persistence.entity.CreditEntryEntity;
import kh.edu.istad.platform.payment.order.persistence.repository.CreditEntryJpaRepository;
import kh.edu.istad.platform.payment.order.port.out.CreditEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreditEntryRepositoryImpl implements CreditEntryRepository {

    private final CreditEntryJpaRepository creditEntryJpaRepository;

    @Override
    public CreditEntry save(CreditEntry creditEntry) {
        creditEntryJpaRepository.save(CreditEntryEntity.builder()
                .id(creditEntry.getId().value())
                .customerId(creditEntry.getCustomerId().value())
                .totalCreditAmount(creditEntry.getTotalCreditAmount().amount())
                .build());
        return creditEntry;
    }

    @Override
    public Optional<CreditEntry> findByCustomerId(CustomerId customerId) {
        return creditEntryJpaRepository.findByCustomerId(customerId.value())
                .map(entity -> CreditEntry.Builder.builder()
                        .id(new CreditEntryId(entity.getId()))
                        .customerId(new CustomerId(entity.getCustomerId()))
                        .totalCreditAmount(new Money(entity.getTotalCreditAmount()))
                        .build());
    }
}