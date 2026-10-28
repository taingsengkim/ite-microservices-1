package kh.edu.istad.platform.customer.client.restapi.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerSegmentRequest(
        @NotBlank
        String type
) {
}
