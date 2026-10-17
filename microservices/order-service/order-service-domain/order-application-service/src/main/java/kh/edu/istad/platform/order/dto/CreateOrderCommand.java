package kh.edu.istad.platform.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderCommand(
        @NotNull(message = "Customer ID is required")
        UUID customerId,

        @NotNull(message = "Business ID is required")
        UUID businessId,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than zero")
        BigDecimal price,

        @Valid
        @NotNull(message = "Order address is required")
        OrderAddressDto address,

        @Valid
        @NotEmpty(message = "Items list cannot be empty")
        List<OrderItemDto> items
) {
    public record OrderAddressDto(
            String street,
            String postalCode,
            String city
    ) {}

    public record OrderItemDto(
            UUID productId,
            int quantity,
            BigDecimal price,
            BigDecimal subTotal
    ) {}
}