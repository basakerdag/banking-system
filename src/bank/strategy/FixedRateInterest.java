package bank.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FixedRateInterest implements InterestStrategy {

    private final BigDecimal monthlyRate;

    public FixedRateInterest(BigDecimal monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    @Override
    public BigDecimal calculateMonthlyInterest(BigDecimal balance) {
        if (balance.signum() <= 0) {
            return BigDecimal.ZERO;
        }
        return balance.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
    }
}