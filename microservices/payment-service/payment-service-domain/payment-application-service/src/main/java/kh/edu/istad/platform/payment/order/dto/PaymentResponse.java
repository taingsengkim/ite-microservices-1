package kh.edu.istad.platform.payment.order.dto;

import kh.edu.istad.platform.payment.order.valueobject.PaymentStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public record PaymentResponse(
        UUID paymentId,
        UUID orderId,
        UUID customerId,
        BigDecimal price,
        PaymentStatus paymentStatus,
        ZonedDateTime createdAt
){}