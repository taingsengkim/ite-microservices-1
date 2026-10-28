package kh.edu.istad.platform.customer.client.restapi.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerUpdateRequest (
        @NotBlank
        String familyName,
        @NotBlank
        String givenName
){
}
