package kh.edu.istad.platform.order.usecase;

import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.ProductId;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.entity.OrderItem;
import kh.edu.istad.platform.order.domain.entity.Product;
import kh.edu.istad.platform.order.domain.event.OrderCreatedEvent;
import kh.edu.istad.platform.order.domain.service.OrderDomainService;
import kh.edu.istad.platform.order.domain.valueobject.StreetAddress;
import kh.edu.istad.platform.order.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.dto.CreateOrderResponse;
import kh.edu.istad.platform.order.port.out.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public CreateOrderResponse execute(CreateOrderCommand command) {
        log.info("Executing CreateOrderUseCase for customer: {}", command.customerId());

        Order order = mapToOrder(command);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order);
        Order saved = orderRepository.save(orderCreatedEvent.getOrder());
        log.info("Order persisted with id: {}", saved.getId().value());

        return new CreateOrderResponse(
                saved.getTrackingId().value(),
                saved.getOrderStatus(),
                "Order created successfully"
        );
    }

    private Order mapToOrder(CreateOrderCommand command) {
        List<OrderItem> items = command.items().stream()
                .map(item -> OrderItem.builder()
                        .product(new Product(
                                new ProductId(item.productId()),
                                "", // name placeholder
                                new Money(item.price())
                        ))
                        .price(new Money(item.price()))
                        .quantity(item.quantity())
                        .subTotal(new Money(item.subTotal()))
                        .build())
                .toList();

        return Order.builder()
                .customerId(new CustomerId(command.customerId()))
                .businessId(new BusinessId(command.businessId()))
                .price(new Money(command.price()))
                .streetAddress(new StreetAddress(
                        UUID.randomUUID(),
                        command.address().street(),
                        command.address().postalCode(),
                        command.address().city()
                ))
                .items(items)
                .build();
    }
}