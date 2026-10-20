
package kh.edu.istad.platform.customer.domain.dto;

public record CustomerCountByStatus(
        String status,
        long total
) {}
  