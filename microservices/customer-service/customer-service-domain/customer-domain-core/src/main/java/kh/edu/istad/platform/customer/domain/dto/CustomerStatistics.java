
package kh.edu.istad.platform.customer.domain.dto;

import java.util.List;

public record CustomerStatistics(
        long totalCustomers,
        List<CustomerCountByStatus> customersByStatus,
        List<CustomerCountBySegment> customersBySegment
) {}
