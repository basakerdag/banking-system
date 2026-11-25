package bank.account;

import bank.exception.AccountFrozenException;
import bank.exception.InvalidAmountException;
import bank.model.AccountStatus;
import bank.model.Customer;
import bank.model.Transaction;
import bank.model.TransactionType;
import bank.strategy.InterestStrategy;
import bank.strategy.WithdrawalRule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Account {

    private final String accountNumber;
    private final Customer owner;
    private final WithdrawalRule withdrawalRule;
    private final InterestStrategy interestStrategy;
    private final List<Transaction> transactions = new ArrayList<>();
    private BigDecimal balance = BigDecimal.ZERO;
    private AccountStatus status = AccountStatus.ACTIVE;

    protected Account(String accountNumber, Customer owner,
                      WithdrawalRule withdrawalRule, InterestStrategy interestStrategy) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.withdrawalRule = withdrawalRule;
        this.interestStrategy = interestStrategy;
    }

    public void deposit(BigDecimal amount, LocalDate date) {
        ensureActive();
        ensurePositive(amount);
        balance = balance.add(amount);
        record(TransactionType.DEPOSIT, amount, date, "Deposit");
    }

    public void withdraw(BigDecimal amount, LocalDate date) {
        ensureActive();
        ensurePositive(amount);
        withdrawalRule.validate(this, amount, date);
        balance = balance.subtract(amount);
        record(TransactionType.WITHDRAWAL, amount, date, "Withdrawal");
    }

    public BigDecimal applyMonthlyInterest(LocalDate date) {
        ensureActive();
        BigDecimal interest = interestStrategy.calculateMonthlyInterest(balance);
        if (interest.signum() > 0) {
            balance = balance.add(interest);
            record(TransactionType.INTEREST, interest, date, "Monthly interest");
        }
        return interest;
    }

    public void freeze() {
        status = AccountStatus.FROZEN;
    }

    public void unfreeze() {
        status = AccountStatus.ACTIVE;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Customer getOwner() {
        return owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    private void ensureActive() {
        if (status == AccountStatus.FROZEN) {
            throw new AccountFrozenException(
                    "Account " + accountNumber + " is frozen and cannot be used.");
        }
    }

    private void ensurePositive(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }
    }

    private void record(TransactionType type, BigDecimal amount, LocalDate date, String description) {
        transactions.add(new Transaction(type, amount, date, description));
    }
}