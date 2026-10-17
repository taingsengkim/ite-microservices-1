package kh.edu.istad.platform.payment.order.usecase;

import kh.edu.istad.common.domain.valueobject.*;
import kh.edu.istad.platform.payment.order.dto.PaymentRequest;
import kh.edu.istad.platform.payment.order.dto.PaymentResponse;
import kh.edu.istad.platform.payment.order.entity.CreditEntry;
import kh.edu.istad.platform.payment.order.entity.CreditHistory;
import kh.edu.istad.platform.payment.order.entity.Payment;
import kh.edu.istad.platform.payment.order.event.PaymentEvent;
import kh.edu.istad.platform.payment.order.exception.PaymentDomainException;
import kh.edu.istad.platform.payment.order.port.out.CreditEntryRepository;
import kh.edu.istad.platform.payment.order.port.out.CreditHistoryRepository;
import kh.edu.istad.platform.payment.order.port.out.PaymentRepository;
import kh.edu.istad.platform.payment.order.service.PaymentDomainService;
import kh.edu.istad.platform.payment.order.service.PaymentDomainServiceImpl;
import kh.edu.istad.platform.payment.order.valueobject.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessPaymentUseCaseImpl implements ProcessPaymentUseCase {

    private final PaymentDomainService paymentDomainService = new PaymentDomainServiceImpl();
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        log.info("Processing payment for order id: {}", paymentRequest.orderId());

        Payment payment = Payment.Builder.builder()
                .orderId(new OrderId(paymentRequest.orderId()))
                .customerId(new CustomerId(paymentRequest.customerId()))
                .price(new Money(paymentRequest.price()))
                .build();

        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistories(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();

        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(
                payment, creditEntry, creditHistories, failureMessages);

        paymentRepository.save(payment);

        if (!failureMessages.isEmpty()) {
            log.error("Payment failed for order id: {}. Errors: {}",
                    payment.getOrderId().value(), String.join(", ", failureMessages));
            throw new PaymentDomainException("Payment failed: " + String.join(", ", failureMessages));
        }

        creditEntryRepository.save(creditEntry);
        if (!creditHistories.isEmpty()) {
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }

        log.info("Payment completed successfully with id: {}", payment.getId().value());
        return toPaymentResponse(payment);
    }

    @Override
    @Transactional
    public PaymentResponse cancelPayment(PaymentRequest paymentRequest) {
        log.info("Cancelling payment for order id: {}", paymentRequest.orderId());

        Payment payment = paymentRepository.findByOrderId(paymentRequest.orderId())
                .orElseThrow(() -> new PaymentDomainException("Payment not found for order id: " + paymentRequest.orderId()));

        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistories(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();

        PaymentEvent paymentEvent = paymentDomainService.validateAndCancelPayment(
                payment, creditEntry, creditHistories, failureMessages);

        paymentRepository.save(payment);

        if (!failureMessages.isEmpty()) {
            log.error("Payment cancellation failed for order id: {}. Errors: {}",
                    payment.getOrderId().value(), String.join(", ", failureMessages));
            throw new PaymentDomainException("Payment cancellation failed: " + String.join(", ", failureMessages));
        }

        creditEntryRepository.save(creditEntry);
        if (!creditHistories.isEmpty()) {
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }

        log.info("Payment cancelled successfully for order id: {}", payment.getOrderId().value());
        return toPaymentResponse(payment);
    }

    private CreditEntry getCreditEntry(CustomerId customerId) {
        return creditEntryRepository.findByCustomerId(customerId)
                .orElseGet(() -> CreditEntry.Builder.builder()
                        .id(new CreditEntryId(UUID.randomUUID()))
                        .customerId(customerId)
                        .totalCreditAmount(new Money(BigDecimal.valueOf(1000.00)))
                        .build());
    }

    private List<CreditHistory> getCreditHistories(CustomerId customerId) {
        return creditHistoryRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    List<CreditHistory> histories = new ArrayList<>();
                    histories.add(CreditHistory.Builder.builder()
                            .id(new CreditHistoryId(UUID.randomUUID()))
                            .customerId(customerId)
                            .amount(new Money(BigDecimal.valueOf(1000.00)))
                            .transactionType(TransactionType.CREDIT)
                            .build());
                    return histories;
                });
    }

    private PaymentResponse toPaymentResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId().value(),
                payment.getOrderId().value(),
                payment.getCustomerId().value(),
                payment.getPrice().amount(),
                payment.getPaymentStatus(),
                payment.getCreatedAt()
        );
    }
}