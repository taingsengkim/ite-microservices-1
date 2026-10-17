package kh.edu.istad.platform.order.restapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        @NotNull(message = "Customer ID is required")
        UUID customerId,

        @NotNull(message = "Business ID is required")
        UUID businessId,

        @NotNull(message = "Total price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than zero")
        BigDecimal price,

        @Valid
        @NotNull(message = "Order address is required")
        OrderAddressRequest address,

        @Valid
        @NotEmpty(message = "Items list cannot be empty")
        List<OrderItemRequest> items
) {
    public record OrderAddressRequest(
            @NotBlank(message = "Street is required")
            String street,

            @NotBlank(message = "Postal code is required")
            String postalCode,

            @NotBlank(message = "City is required")
            String city
    ) {}

    public record OrderItemRequest(
            @NotNull(message = "Product ID is required")
            UUID productId,

            @Min(value = 1, message = "Quantity must be at least 1")
            int quantity,

            @NotNull(message = "Price is required")
            @DecimalMin(value = "0.01", message = "Price must be greater than zero")
            BigDecimal price,

            @NotNull(message = "SubTotal is required")
            @DecimalMin(value = "0.01", message = "SubTotal must be greater than zero")
            BigDecimal subTotal
    ) {}
}