package bank.account;

import bank.model.Customer;
import bank.strategy.FixedRateInterest;
import bank.strategy.OverdraftWithdrawalRule;

import java.math.BigDecimal;

public class CheckingAccount extends Account {

    private static final BigDecimal OVERDRAFT_LIMIT = new BigDecimal("500.00");
    private static final BigDecimal MONTHLY_RATE = new BigDecimal("0.001");

    public CheckingAccount(String accountNumber, Customer owner) {
        super(accountNumber, owner,
                new OverdraftWithdrawalRule(OVERDRAFT_LIMIT),
                new FixedRateInterest(MONTHLY_RATE));
    }
}