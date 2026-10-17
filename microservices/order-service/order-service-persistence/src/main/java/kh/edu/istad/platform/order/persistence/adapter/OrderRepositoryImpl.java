package kh.edu.istad.platform.order.persistence.adapter;

import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.persistence.entity.OrderEntity;
import kh.edu.istad.platform.order.persistence.mapper.OrderPersistenceMapper;
import kh.edu.istad.platform.order.persistence.repository.OrderJpaRepository;
import kh.edu.istad.platform.order.port.out.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private final OrderPersistenceMapper mapper;

    @Override
    public Order save(Order order) {
        log.info("Persisting order into PostgreSQL with id: {}", order.getId().value());
        OrderEntity orderEntity = mapper.orderToOrderEntity(order);
        OrderEntity savedEntity = jpaRepository.save(orderEntity);
        return mapper.orderEntityToOrder(savedEntity);
    }
}