package kh.edu.istad.platform.business.domain.dto;

import java.util.UUID;

public record CreateBusinessResponse(
        UUID businessId,
        String message
) {}

