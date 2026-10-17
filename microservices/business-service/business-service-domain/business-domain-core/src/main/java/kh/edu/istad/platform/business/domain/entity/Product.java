package kh.edu.istad.platform.business.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.ProductId;
import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;

public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Boolean getAvailable() {
        return available;
    }

    private Integer quantity;
    private Boolean available;

    // Safe boolean check for availability and stock
    public boolean isAvailable() {
        return Boolean.TRUE.equals(this.available) && (this.quantity == null || this.quantity > 0);
    }

    // Method to update catalog details safely
    public void updateDetails(String name, Money price, Boolean available) {
        if (name == null || name.isBlank()) {
            throw new BusinessDomainException("Product name cannot be empty");
        }
        if (price == null || !price.isGreaterThanZero()) {
            throw new BusinessDomainException("Product price must be greater than zero");
        }
        this.name = name;
        this.price = price;
        this.available = available != null ? available : false;
    }

    // Inventory reduction (if doing local inventory checks)
    public void deductQuantity(int amount) {
        if (this.quantity == null || this.quantity < amount) {
            throw new BusinessDomainException("Insufficient quantity for product: " + this.name);
        }
        this.quantity -= amount;
        if (this.quantity == 0) {
            this.available = false;
        }
    }

    private Product(Builder builder) {
        super.setId(builder.id);
        this.name = builder.name;
        this.price = builder.price;
        this.quantity = builder.quantity != null ? builder.quantity : 0;
        this.available = builder.available != null ? builder.available : false;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private ProductId id;
        private String name;
        private Money price;
        private Integer quantity;
        private Boolean available;

        private Builder() {}

        public Builder id(ProductId val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder quantity(Integer val) {
            quantity = val;
            return this;
        }

        public Builder available(Boolean val) {
            available = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}