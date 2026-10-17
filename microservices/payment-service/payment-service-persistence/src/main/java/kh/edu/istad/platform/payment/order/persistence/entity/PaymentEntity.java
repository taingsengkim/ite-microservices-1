package kh.edu.istad.platform.payment.order.persistence.entity;

import jakarta.persistence.*;
import kh.edu.istad.platform.payment.order.valueobject.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEntity {

    @Id
    private UUID id;

    private UUID orderId;

    private UUID customerId;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private ZonedDateTime createdAt;
}