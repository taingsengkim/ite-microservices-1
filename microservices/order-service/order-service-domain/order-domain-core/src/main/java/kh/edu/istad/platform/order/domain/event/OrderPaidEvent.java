package kh.edu.istad.platform.order.domain.event;

import kh.edu.istad.platform.order.domain.entity.Order;
import java.time.ZonedDateTime;

public class OrderPaidEvent extends OrderEvent {
    public OrderPaidEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}