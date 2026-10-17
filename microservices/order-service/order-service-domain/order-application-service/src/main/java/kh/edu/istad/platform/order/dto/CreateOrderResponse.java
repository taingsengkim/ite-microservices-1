package kh.edu.istad.platform.order.dto;

import kh.edu.istad.platform.order.domain.valueobject.OrderStatus;

import java.util.UUID;

public record CreateOrderResponse(
        UUID orderTrackingId,
        OrderStatus orderStatus,
        String message
) {}