
package kh.edu.istad.platform.customer.domain.dto;

public record CustomerCountBySegment(
        String segmentType,
        long total
) {}
  