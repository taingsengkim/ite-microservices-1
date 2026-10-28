package kh.edu.istad.platform.customer.client.dto;

import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String username,
        String familyName,
        String givenName,
        String email,
        String phoneNumber,
        String status
) {
}