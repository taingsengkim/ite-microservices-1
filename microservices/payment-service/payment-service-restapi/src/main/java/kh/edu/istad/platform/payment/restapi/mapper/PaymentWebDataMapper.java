package kh.edu.istad.platform.payment.restapi.mapper;

import kh.edu.istad.platform.payment.order.dto.PaymentRequest;
import kh.edu.istad.platform.payment.order.dto.PaymentResponse;
import kh.edu.istad.platform.payment.restapi.dto.CreatePaymentWebDto;
import kh.edu.istad.platform.payment.restapi.dto.PaymentWebResponse;
import org.springframework.stereotype.Component;

@Component
public class PaymentWebDataMapper {

    public PaymentRequest toPaymentRequest(CreatePaymentWebDto dto) {
        return new PaymentRequest(
                dto.orderId(),
                dto.customerId(),
                dto.price()
        );
    }

    public PaymentWebResponse toPaymentWebResponse(PaymentResponse response) {
        return new PaymentWebResponse(
                response.paymentId(),
                response.orderId(),
                response.customerId(),
                response.price(),
                response.paymentStatus(),
                response.createdAt()
        );
    }
}