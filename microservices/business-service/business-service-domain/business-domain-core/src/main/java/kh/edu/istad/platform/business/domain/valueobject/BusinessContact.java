package kh.edu.istad.platform.business.domain.valueobject;

import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;

import java.util.regex.Pattern;

public record BusinessContact(String email, String phone, String address) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public BusinessContact {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessDomainException("A valid business email address must be provided");
        }
        if (phone == null || phone.isBlank()) {
            throw new BusinessDomainException("Business phone number must not be empty");
        }
        if (address == null || address.isBlank()) {
            throw new BusinessDomainException("Business physical address must not be empty");
        }
        email = email.trim().toLowerCase();
        phone = phone.trim();
        address = address.trim();
    }
}