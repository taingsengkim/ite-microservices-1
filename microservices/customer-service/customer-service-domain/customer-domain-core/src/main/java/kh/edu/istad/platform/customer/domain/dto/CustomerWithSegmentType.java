package kh.edu.istad.platform.customer.client.domain.dto;

import kh.edu.istad.platform.customer.client.domain.entity.Customer;

public record CustomerWithSegmentType(
        Customer customer,
        String customerSegmentType
) {
}