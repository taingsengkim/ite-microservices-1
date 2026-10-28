package kh.edu.istad.platform.customer.client.domain.dto;

public record CustomerFilter(
        String username,
        String familyName,
        String givenName,
        String email,
        String phoneNumber,
        String customerStatus
) {
}