package bank.account;

import bank.model.Customer;
import bank.strategy.FixedRateInterest;
import bank.strategy.MonthlyLimitWithdrawalRule;

import java.math.BigDecimal;

public class SavingsAccount extends Account {

    private static final int MAX_WITHDRAWALS_PER_MONTH = 3;
    private static final BigDecimal MONTHLY_RATE = new BigDecimal("0.005");

    public SavingsAccount(String accountNumber, Customer owner) {
        super(accountNumber, owner,
                new MonthlyLimitWithdrawalRule(MAX_WITHDRAWALS_PER_MONTH),
                new FixedRateInterest(MONTHLY_RATE));
    }
}