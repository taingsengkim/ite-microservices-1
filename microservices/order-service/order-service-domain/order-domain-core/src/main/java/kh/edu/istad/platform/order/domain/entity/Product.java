package kh.edu.istad.platform.order.domain.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.ProductId;
import kh.edu.istad.platform.order.domain.exception.OrderDomainException;

public class Product extends BaseEntity<ProductId> {
    private String name;

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    private Money price;

    public Product(ProductId productId) {
        super.setId(productId);
    }

    public Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }

    public void updateWithConfirmedNameAndPrice(String name, Money price) {
        if (name == null || name.isBlank()) {
            throw new OrderDomainException("Product name cannot be empty");
        }
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Product price must be greater than zero");
        }
        this.name = name;
        this.price = price;
    }
}