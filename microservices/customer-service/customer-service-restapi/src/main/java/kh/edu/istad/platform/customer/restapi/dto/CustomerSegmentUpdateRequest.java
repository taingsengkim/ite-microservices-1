
package kh.edu.istad.platform.customer.restapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerSegmentUpdateRequest(
        @NotBlank(message = "Customer segment type must not be blank")
        @Size(max = 50, message = "Customer segment type must not exceed 50 characters")
        String type
) {}
