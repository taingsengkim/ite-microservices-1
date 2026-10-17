package kh.edu.istad.platform.payment.order.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "credit_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditEntryEntity {

    @Id
    private UUID id;

    private UUID customerId;

    private BigDecimal totalCreditAmount;
}