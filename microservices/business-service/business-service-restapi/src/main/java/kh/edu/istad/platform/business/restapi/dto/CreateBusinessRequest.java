package kh.edu.istad.platform.business.restapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateBusinessRequest(
        @NotBlank(message = "Legal name cannot be blank")
        String legalName,

        String displayName,

        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Phone cannot be blank")
        String phone,

        @NotBlank(message = "Address cannot be blank")
        String address,

        @Valid
        @NotEmpty(message = "Products list cannot be empty")
        List<ProductRequest> products
) {
    public record ProductRequest(
            UUID id,

            @NotBlank(message = "Product name cannot be blank")
            String name,

            @NotNull(message = "Price is required")
            @DecimalMin(value = "0.01", message = "Price must be greater than zero")
            BigDecimal price,

            Integer quantity,
            Boolean available
    ) {}
}