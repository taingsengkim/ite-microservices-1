package kh.edu.istad.platform.business.domain.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateBusinessCommand(
        @NotBlank(message = "Legal name is required")
        String legalName,

        String displayName,

        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Phone number is required")
        String phone,

        @NotBlank(message = "Address is required")
        String address,

        @Valid
        @NotEmpty(message = "Product catalog cannot be empty")
        List<ProductDto> products
) {
    public record ProductDto(
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