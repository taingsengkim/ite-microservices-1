
package kh.edu.istad.platform.customer.client.domain.dto;

public record CustomerCountByStatus(
        String status,
        long total
) {}
  