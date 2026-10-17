package kh.edu.istad.platform.payment.order.service;

import kh.edu.istad.platform.payment.order.entity.CreditEntry;
import kh.edu.istad.platform.payment.order.entity.CreditHistory;
import kh.edu.istad.platform.payment.order.entity.Payment;
import kh.edu.istad.platform.payment.order.event.PaymentEvent;

import java.util.List;

public interface PaymentDomainService {

    PaymentEvent validateAndInitiatePayment(Payment payment,
                                            CreditEntry creditEntry,
                                            List<CreditHistory> creditHistories,
                                            List<String> failureMessages);

    PaymentEvent validateAndCancelPayment(Payment payment,
                                          CreditEntry creditEntry,
                                          List<CreditHistory> creditHistories,
                                          List<String> failureMessages);
}