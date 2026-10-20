package kh.edu.istad.platform.customer.domain.dto;

import kh.edu.istad.platform.customer.domain.entity.Customer;

public record CustomerWithSegmentType(
        Customer customer,
        String customerSegmentType
) {
}