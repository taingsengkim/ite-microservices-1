package kh.edu.istad.platform.payment.restapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentWebDto(
        @NotNull(message = "Order ID is required")
        UUID orderId,

        @NotNull(message = "Customer ID is required")
        UUID customerId,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price
) {}