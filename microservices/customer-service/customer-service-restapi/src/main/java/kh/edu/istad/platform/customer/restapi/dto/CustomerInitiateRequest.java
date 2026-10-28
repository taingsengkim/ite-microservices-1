package kh.edu.istad.platform.customer.client.restapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CustomerInitiateRequest(
        @NotBlank
        String username,

        @NotBlank
        String familyName,

        @NotBlank
        String givenName,

        @NotBlank
        String email,

        @NotBlank
        String phoneNumber,

        @NotNull
        UUID customerSegmentId
) {
}