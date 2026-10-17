package kh.edu.istad.platform.payment.order.entity;

import kh.edu.istad.common.domain.entity.BaseEntity;
import kh.edu.istad.common.domain.valueobject.CreditEntryId;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.Money;

public class CreditEntry extends BaseEntity<CreditEntryId> {
    private final CustomerId customerId;
    private Money totalCreditAmount;

    public void addCreditAmount(Money amount) {
        this.totalCreditAmount = this.totalCreditAmount.add(amount);
    }

    public void subtractCreditAmount(Money amount) {
        this.totalCreditAmount = this.totalCreditAmount.subtract(amount);
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getTotalCreditAmount() {
        return totalCreditAmount;
    }

    private CreditEntry(Builder builder) {
        super.setId(builder.id);
        this.customerId = builder.customerId;
        this.totalCreditAmount = builder.totalCreditAmount;
    }

    public static final class Builder {
        private CreditEntryId id;
        private CustomerId customerId;
        private Money totalCreditAmount;

        private Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(CreditEntryId val) {
            id = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder totalCreditAmount(Money val) {
            totalCreditAmount = val;
            return this;
        }

        public CreditEntry build() {
            return new CreditEntry(this);
        }
    }
}