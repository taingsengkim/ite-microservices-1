
package kh.edu.istad.platform.customer.client.domain.dto;

public record CustomerCountBySegment(
        String segmentType,
        long total
) {}
  