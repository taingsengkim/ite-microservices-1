
package kh.edu.istad.platform.customer.domain.dto;

import java.util.UUID;

public record UpdateCustomerCommand(
        String familyName,
        String givenName,
        String email,
        String phoneNumber,
        UUID customerSegmentId
) {
}
