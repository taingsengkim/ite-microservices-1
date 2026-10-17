package kh.edu.istad.common.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount) {

    public static final Money ZERO = new Money(BigDecimal.ZERO);
    public Money {
        Objects.requireNonNull(amount, "Amount must not be null");
        amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public boolean isGreaterThanZero() {
        return this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThanOrEqualTo(Money money) {
        return money != null && this.amount.compareTo(money.amount()) >= 0;
    }

    public Money add(Money money) {
        Objects.requireNonNull(money, "Money to add must not be null");
        return new Money(this.amount.add(money.amount()));
    }

    public Money subtract(Money money) {
        Objects.requireNonNull(money, "Money to subtract must not be null");
        return new Money(this.amount.subtract(money.amount()));
    }

    public boolean isGreaterThan(Money money) {
        return money != null && this.amount.compareTo(money.amount()) > 0;
    }

    public Money multiply(int multiplier) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)));
    }
}