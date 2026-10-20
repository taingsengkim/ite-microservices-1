package kh.edu.istad.platform.customer.domain.dto;

public record CustomerFilter(
        String username,
        String familyName,
        String givenName,
        String email,
        String phoneNumber,
        String customerStatus
) {
}