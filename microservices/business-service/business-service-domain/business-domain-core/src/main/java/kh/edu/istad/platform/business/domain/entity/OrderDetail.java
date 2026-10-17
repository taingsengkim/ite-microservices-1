package kh.edu.istad.platform.business.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.common.domain.valueobject.OrderStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderDetail extends BaseEntity<OrderId> {
    private final OrderStatus orderStatus;
    private final Money totalAmount;
    private final List<Product> products;

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public List<Product> getProducts() {
        return products;
    }

    private OrderDetail(Builder builder) {
        super.setId(builder.id);
        this.orderStatus = builder.orderStatus;
        this.totalAmount = builder.totalAmount;
        this.products = builder.products != null
                ? Collections.unmodifiableList(new ArrayList<>(builder.products))
                : Collections.emptyList();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private OrderId id;
        private OrderStatus orderStatus;
        private Money totalAmount;
        private List<Product> products;

        private Builder() {}

        public Builder id(OrderId val) {
            id = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder totalAmount(Money val) {
            totalAmount = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public OrderDetail build() {
            return new OrderDetail(this);
        }
    }
}