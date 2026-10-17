package kh.edu.istad.platform.payment.order.service;

import kh.edu.istad.common.domain.valueobject.CreditHistoryId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.platform.payment.order.entity.CreditEntry;
import kh.edu.istad.platform.payment.order.entity.CreditHistory;
import kh.edu.istad.platform.payment.order.entity.Payment;
import kh.edu.istad.platform.payment.order.event.PaymentCancelledEvent;
import kh.edu.istad.platform.payment.order.event.PaymentCompletedEvent;
import kh.edu.istad.platform.payment.order.event.PaymentEvent;
import kh.edu.istad.platform.payment.order.event.PaymentFailedEvent;
import kh.edu.istad.platform.payment.order.valueobject.PaymentStatus;
import kh.edu.istad.platform.payment.order.valueobject.TransactionType;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class PaymentDomainServiceImpl implements PaymentDomainService {

    @Override
    public PaymentEvent validateAndInitiatePayment(Payment payment,
                                                   CreditEntry creditEntry,
                                                   List<CreditHistory> creditHistories,
                                                   List<String> failureMessages) {
        payment.validatePayment(failureMessages);
        payment.initializePayment();
        validateCreditEntry(payment, creditEntry, failureMessages);
        subtractCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);
        validateCreditHistory(creditEntry, creditHistories, failureMessages);

        if (failureMessages.isEmpty()) {
            payment.updateStatus(PaymentStatus.COMPLETED);
            return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")));
        } else {
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), failureMessages);
        }
    }

    @Override
    public PaymentEvent validateAndCancelPayment(Payment payment,
                                                 CreditEntry creditEntry,
                                                 List<CreditHistory> creditHistories,
                                                 List<String> failureMessages) {
        payment.validatePayment(failureMessages);
        addCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.CREDIT);

        if (failureMessages.isEmpty()) {
            payment.updateStatus(PaymentStatus.CANCELLED);
            return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")));
        } else {
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), failureMessages);
        }
    }

    private void validateCreditEntry(Payment payment, CreditEntry creditEntry, List<String> failureMessages) {
        if (payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
            failureMessages.add("Customer with id: " + payment.getCustomerId().value()
                    + " doesn't have enough credit for payment!");
        }
    }

    private void subtractCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.subtractCreditAmount(payment.getPrice());
    }

    private void addCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.addCreditAmount(payment.getPrice());
    }

    private void updateCreditHistory(Payment payment,
                                     List<CreditHistory> creditHistories,
                                     TransactionType transactionType) {
        creditHistories.add(CreditHistory.Builder.builder()
                .id(new CreditHistoryId(UUID.randomUUID()))
                .customerId(payment.getCustomerId())
                .amount(payment.getPrice())
                .transactionType(transactionType)
                .build());
    }

    private void validateCreditHistory(CreditEntry creditEntry,
                                       List<CreditHistory> creditHistories,
                                       List<String> failureMessages) {
        Money totalDebitHistory = getTotalHistoryAmount(creditHistories, TransactionType.DEBIT);
        Money totalCreditHistory = getTotalHistoryAmount(creditHistories, TransactionType.CREDIT);

        if (totalDebitHistory.isGreaterThan(totalCreditHistory)) {
            failureMessages.add("Customer credit history balance is inconsistent!");
        }
    }

    private Money getTotalHistoryAmount(List<CreditHistory> creditHistories, TransactionType transactionType) {
        return creditHistories.stream()
                .filter(history -> transactionType == history.getTransactionType())
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);
    }
}