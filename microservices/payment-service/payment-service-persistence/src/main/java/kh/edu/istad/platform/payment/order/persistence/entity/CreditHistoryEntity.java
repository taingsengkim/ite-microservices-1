package kh.edu.istad.platform.payment.order.persistence.entity;

import jakarta.persistence.*;
import kh.edu.istad.platform.payment.order.valueobject.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "credit_histories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditHistoryEntity {

    @Id
    private UUID id;

    private UUID customerId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
}