package kh.edu.istad.platform.payment.order.usecase;

import kh.edu.istad.platform.payment.order.dto.PaymentRequest;
import kh.edu.istad.platform.payment.order.dto.PaymentResponse;

public interface ProcessPaymentUseCase {

    PaymentResponse processPayment(PaymentRequest paymentRequest);

    PaymentResponse cancelPayment(PaymentRequest paymentRequest);
}