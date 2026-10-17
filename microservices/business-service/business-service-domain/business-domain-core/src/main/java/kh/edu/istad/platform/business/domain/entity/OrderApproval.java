package kh.edu.istad.platform.business.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.platform.business.domain.valueobject.OrderApprovalId;
import kh.edu.istad.platform.business.domain.valueobject.OrderApprovalStatus;

import java.util.Objects;

public class OrderApproval extends BaseEntity<OrderApprovalId> {
    public BusinessId getBusinessId() {
        return businessId;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public OrderApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    private final BusinessId businessId;
    private final OrderId orderId;
    private final OrderApprovalStatus approvalStatus;

    public OrderApproval(OrderApprovalId orderApprovalId, BusinessId businessId, OrderId orderId, OrderApprovalStatus approvalStatus) {
        super.setId(Objects.requireNonNull(orderApprovalId, "OrderApprovalId must not be null"));
        this.businessId = Objects.requireNonNull(businessId, "BusinessId must not be null");
        this.orderId = Objects.requireNonNull(orderId, "OrderId must not be null");
        this.approvalStatus = Objects.requireNonNull(approvalStatus, "ApprovalStatus must not be null");
    }

    private OrderApproval(Builder builder) {
        this(builder.orderApprovalId, builder.businessId, builder.orderId, builder.approvalStatus);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private OrderApprovalId orderApprovalId;
        private BusinessId businessId;
        private OrderId orderId;
        private OrderApprovalStatus approvalStatus;

        private Builder() {}

        public Builder orderApprovalId(OrderApprovalId val) {
            orderApprovalId = val;
            return this;
        }

        public Builder businessId(BusinessId val) {
            businessId = val;
            return this;
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder approvalStatus(OrderApprovalStatus val) {
            approvalStatus = val;
            return this;
        }

        public OrderApproval build() {
            return new OrderApproval(this);
        }
    }
}