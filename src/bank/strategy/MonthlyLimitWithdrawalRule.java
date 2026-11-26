package bank.strategy;

import bank.account.Account;
import bank.exception.InsufficientFundsException;
import bank.exception.WithdrawalLimitExceededException;
import bank.model.Transaction;
import bank.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public class MonthlyLimitWithdrawalRule implements WithdrawalRule {

    private final int maxWithdrawalsPerMonth;

    public MonthlyLimitWithdrawalRule(int maxWithdrawalsPerMonth) {
        this.maxWithdrawalsPerMonth = maxWithdrawalsPerMonth;
    }

    @Override
    public void validate(Account account, BigDecimal amount, LocalDate date) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                    "Savings account balance is not enough for this withdrawal.");
        }
        YearMonth month = YearMonth.from(date);
        long count = account.getTransactions().stream()
                .filter(t -> t.getType() == TransactionType.WITHDRAWAL)
                .filter(t -> YearMonth.from(t.getDate()).equals(month))
                .count();
        if (count >= maxWithdrawalsPerMonth) {
            throw new WithdrawalLimitExceededException(
                    "Savings account allows only " + maxWithdrawalsPerMonth + " withdrawals per month.");
        }
    }
}