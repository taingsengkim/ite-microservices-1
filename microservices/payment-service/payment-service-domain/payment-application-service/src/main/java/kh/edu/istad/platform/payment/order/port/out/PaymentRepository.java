package kh.edu.istad.platform.payment.order.port.out;

import kh.edu.istad.platform.payment.order.entity.Payment;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findByOrderId(UUID orderId);
}