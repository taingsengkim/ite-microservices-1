package kh.edu.istad.common.restapi.dto;

public record FieldErrorResponse(
        String field,
        String code,
        String reason
) {
}
