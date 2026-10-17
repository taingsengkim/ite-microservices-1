package kh.edu.istad.platform.business.restapi.dto;

import java.util.UUID;

public record CreateBusinessWebResponse(
        UUID businessId,
        String message
) {}