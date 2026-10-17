package kh.edu.istad.platform.payment.order.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.common.domain.valueobject.PaymentId;
import kh.edu.istad.platform.payment.order.entity.Payment;
import kh.edu.istad.platform.payment.order.persistence.entity.PaymentEntity;
import kh.edu.istad.platform.payment.order.persistence.repository.PaymentJpaRepository;
import kh.edu.istad.platform.payment.order.port.out.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = PaymentEntity.builder()
                .id(payment.getId().value())
                .orderId(payment.getOrderId().value())
                .customerId(payment.getCustomerId().value())
                .price(payment.getPrice().amount())
                .status(payment.getPaymentStatus())
                .createdAt(payment.getCreatedAt())
                .build();
        paymentJpaRepository.save(entity);
        return payment;
    }

    @Override
    public Optional<Payment> findByOrderId(UUID orderId) {
        return paymentJpaRepository.findByOrderId(orderId)
                .map(entity -> Payment.Builder.builder()
                        .id(new PaymentId(entity.getId()))
                        .orderId(new OrderId(entity.getOrderId()))
                        .customerId(new CustomerId(entity.getCustomerId()))
                        .price(new Money(entity.getPrice()))
                        .paymentStatus(entity.getStatus())
                        .createdAt(entity.getCreatedAt())
                        .build());
    }
}