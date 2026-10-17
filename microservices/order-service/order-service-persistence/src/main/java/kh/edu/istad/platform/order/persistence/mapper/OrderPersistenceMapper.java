package kh.edu.istad.platform.order.persistence.mapper;

import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.common.domain.valueobject.OrderItemId;
import kh.edu.istad.common.domain.valueobject.ProductId;
import kh.edu.istad.common.domain.valueobject.TrackingId;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.entity.OrderItem;
import kh.edu.istad.platform.order.domain.entity.Product;
import kh.edu.istad.platform.order.domain.valueobject.StreetAddress;
import kh.edu.istad.platform.order.persistence.entity.OrderAddressEntity;
import kh.edu.istad.platform.order.persistence.entity.OrderEntity;
import kh.edu.istad.platform.order.persistence.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderPersistenceMapper {
    public OrderEntity orderToOrderEntity(Order order) {
        OrderAddressEntity addressEntity = OrderAddressEntity.builder()
                .id(order.getStreetAddress().id())
                .street(order.getStreetAddress().street())
                .postalCode(order.getStreetAddress().postalCode())
                .city(order.getStreetAddress().city())
                .build();

        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().value())
                .customerId(order.getCustomerId().value())
                .businessId(order.getBusinessId().value())
                .trackingId(order.getTrackingId().value())
                .price(order.getPrice().amount())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages() != null ? String.join(";", order.getFailureMessages()) : "")
                .address(addressEntity)
                .build();

        List<OrderItemEntity> itemEntities = order.getItems() != null
                ? order.getItems().stream()
                .map(item -> OrderItemEntity.builder()
                        .id(item.getId() != null ? item.getId().value() : null)
                        .order(orderEntity)
                        .productId(item.getProduct().getId().value())
                        .price(item.getPrice().amount())
                        .quantity(item.getQuantity())
                        .subTotal(item.getSubTotal().amount())
                        .build())
                .toList()
                : new ArrayList<>();

        orderEntity.setItems(itemEntities);
        return orderEntity;
    }
    public Order orderEntityToOrder(OrderEntity entity) {
        StreetAddress address = new StreetAddress(
                entity.getAddress().getId(),
                entity.getAddress().getStreet(),
                entity.getAddress().getPostalCode(),
                entity.getAddress().getCity()
        );

        List<OrderItem> items = entity.getItems() != null
                ? entity.getItems().stream()
                .map(item -> OrderItem.builder()
                        .id(new OrderItemId(item.getId()))
                        .product(new Product(new ProductId(item.getProductId())))
                        .price(new Money(item.getPrice()))
                        .quantity(item.getQuantity())
                        .subTotal(new Money(item.getSubTotal()))
                        .build())
                .toList()
                : new ArrayList<>();

        return Order.builder()
                .id(new OrderId(entity.getId()))
                .customerId(new CustomerId(entity.getCustomerId()))
                .businessId(new BusinessId(entity.getBusinessId()))
                .trackingId(new TrackingId(entity.getTrackingId()))
                .streetAddress(address)
                .price(new Money(entity.getPrice()))
                .orderStatus(entity.getOrderStatus())
                .items(items)
                .build();
    }
}