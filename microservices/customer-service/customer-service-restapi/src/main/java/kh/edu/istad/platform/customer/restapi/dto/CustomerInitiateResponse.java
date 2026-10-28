package kh.edu.istad.platform.customer.client.restapi.dto;

import java.util.UUID;

public record CustomerInitiateResponse(
        UUID customerId,
        String username,
        String familyName,
        String givenName,
        String email,
        String phoneNumber,
        String customerSegmentType
) {
}