package kh.edu.istad.platform.business.domain.entity;

import kh.edu.istad.common.domain.entity.AggregateRoot;
import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;
import kh.edu.istad.platform.business.domain.valueobject.BusinessContact;
import kh.edu.istad.platform.business.domain.valueobject.BusinessName;
import kh.edu.istad.platform.business.domain.valueobject.OrderApprovalId;
import kh.edu.istad.platform.business.domain.valueobject.OrderApprovalStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Business extends AggregateRoot<BusinessId> {
    private BusinessName businessName;
    private BusinessContact businessContact;
    private OrderApproval orderApproval;
    private Boolean active;
    private OrderDetail orderDetail;
    private List<Product> products;

    public void validateBusiness() {
        if (getId() == null) {
            throw new BusinessDomainException("Business ID must not be null");
        }
        if (businessName == null) {
            throw new BusinessDomainException("Business name must not be null");
        }
        if (businessContact == null) {
            throw new BusinessDomainException("Business contact information must be provided");
        }
    }

    public void initializeBusiness() {
        if (getId() == null) {
            setId(new BusinessId(UUID.randomUUID()));
        }
        this.active = true;
        if (this.products == null) {
            this.products = new ArrayList<>();
        }
    }

    public BusinessName getBusinessName() {
        return businessName;
    }

    public BusinessContact getBusinessContact() {
        return businessContact;
    }

    public OrderApproval getOrderApproval() {
        return orderApproval;
    }

    public Boolean getActive() {
        return active;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void validateOrder(List<String> failureMessages) {
        if (active == null || !active) {
            failureMessages.add("Business with id: " + getId().value() + " is currently inactive");
            return;
        }

        if (orderDetail == null || orderDetail.getProducts() == null || orderDetail.getProducts().isEmpty()) {
            failureMessages.add("Order detail contains no items");
            return;
        }

        if (this.products == null || this.products.isEmpty()) {
            failureMessages.add("Business has no available catalog products");
            return;
        }

        Map<UUID, Product> catalogProductMap = this.products.stream()
                .collect(Collectors.toMap(p -> p.getId().value(), Function.identity()));

        for (Product orderedProduct : orderDetail.getProducts()) {
            Product catalogProduct = catalogProductMap.get(orderedProduct.getId().value());
            if (catalogProduct == null) {
                failureMessages.add("Product with id " + orderedProduct.getId().value() + " does not belong to this business");
            } else if (!catalogProduct.isAvailable()) {
                failureMessages.add("Product: " + catalogProduct.getName() + " is currently unavailable");
            } else if (!catalogProduct.getPrice().equals(orderedProduct.getPrice())) {
                failureMessages.add("Price mismatch for product: " + catalogProduct.getName());
            }
        }
    }

    public void constructOrderApproval(OrderApprovalStatus orderApprovalStatus) {
        this.orderApproval = OrderApproval.builder()
                .orderApprovalId(new OrderApprovalId(UUID.randomUUID()))
                .businessId(this.getId())
                .orderId(this.orderDetail.getId())
                .approvalStatus(orderApprovalStatus)
                .build();
    }

    private Business(Builder builder) {
        super.setId(builder.id);
        businessName = builder.businessName;
        businessContact = builder.businessContact;
        orderApproval = builder.orderApproval;
        active = builder.active;
        orderDetail = builder.orderDetail;
        products = builder.products != null ? builder.products : new ArrayList<>();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private BusinessId id;
        private BusinessName businessName;
        private BusinessContact businessContact;
        private OrderApproval orderApproval;
        private Boolean active;
        private OrderDetail orderDetail;
        private List<Product> products;

        private Builder() {}

        public Builder id(BusinessId val) {
            id = val;
            return this;
        }

        public Builder businessName(BusinessName val) {
            businessName = val;
            return this;
        }

        public Builder businessContact(BusinessContact val) {
            businessContact = val;
            return this;
        }

        public Builder orderApproval(OrderApproval val) {
            orderApproval = val;
            return this;
        }

        public Builder active(Boolean val) {
            active = val;
            return this;
        }

        public Builder orderDetail(OrderDetail val) {
            orderDetail = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Business build() {
            return new Business(this);
        }
    }
}