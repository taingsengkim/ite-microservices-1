package kh.edu.istad.common.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record OrderItemId(UUID value) {
    public OrderItemId {
        Objects.requireNonNull(value, "OrderItemId value must not be null");
    }

    public static OrderItemId of(UUID value) {
        return new OrderItemId(value);
    }
}