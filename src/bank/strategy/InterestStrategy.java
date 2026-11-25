package bank.strategy;

import java.math.BigDecimal;

public interface InterestStrategy {

    BigDecimal calculateMonthlyInterest(BigDecimal balance);
}