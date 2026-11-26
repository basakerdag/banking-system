package bank.strategy;

import bank.account.Account;
import bank.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OverdraftWithdrawalRule implements WithdrawalRule {

    private final BigDecimal overdraftLimit;

    public OverdraftWithdrawalRule(BigDecimal overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void validate(Account account, BigDecimal amount, LocalDate date) {
        BigDecimal balanceAfter = account.getBalance().subtract(amount);
        if (balanceAfter.compareTo(overdraftLimit.negate()) < 0) {
            throw new InsufficientFundsException(
                    "Withdrawal exceeds the overdraft limit of " + overdraftLimit + ".");
        }
    }
}