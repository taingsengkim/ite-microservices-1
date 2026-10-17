package kh.edu.istad.platform.payment.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.payment.order.dto.PaymentRequest;
import kh.edu.istad.platform.payment.order.dto.PaymentResponse;
import kh.edu.istad.platform.payment.restapi.dto.CreatePaymentWebDto;
import kh.edu.istad.platform.payment.restapi.dto.PaymentWebResponse;
import kh.edu.istad.platform.payment.restapi.mapper.PaymentWebDataMapper;
import kh.edu.istad.platform.payment.order.usecase.ProcessPaymentUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final ProcessPaymentUseCase processPaymentUseCase;
    private final PaymentWebDataMapper paymentWebDataMapper;

    @PostMapping
    public ResponseEntity<PaymentWebResponse> processPayment(@Valid @RequestBody CreatePaymentWebDto createPaymentWebDto) {
        log.info("REST: Initiating payment for order ID: {}", createPaymentWebDto.orderId());
        PaymentRequest request = paymentWebDataMapper.toPaymentRequest(createPaymentWebDto);
        PaymentResponse response = processPaymentUseCase.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentWebDataMapper.toPaymentWebResponse(response));
    }

    @PostMapping("/cancel")
    public ResponseEntity<PaymentWebResponse> cancelPayment(@Valid @RequestBody CreatePaymentWebDto createPaymentWebDto) {
        log.info("REST: Cancelling payment for order ID: {}", createPaymentWebDto.orderId());
        PaymentRequest request = paymentWebDataMapper.toPaymentRequest(createPaymentWebDto);
        PaymentResponse response = processPaymentUseCase.cancelPayment(request);
        return ResponseEntity.ok(paymentWebDataMapper.toPaymentWebResponse(response));
    }
}