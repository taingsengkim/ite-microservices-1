
package kh.edu.istad.platform.customer.restapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CustomerUpdateRequest(
        @Size(max = 100, message = "Family name must not exceed 100 characters")
        String familyName,

        @Size(max = 100, message = "Given name must not exceed 100 characters")
        String givenName,

        @Email(message = "Email must be valid")
        @Size(max = 254, message = "Email must not exceed 254 characters")
        String email,

        @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Phone number must contain 8 to 15 digits, optionally starting with +")
        String phoneNumber,

        UUID customerSegmentId
) {
}
