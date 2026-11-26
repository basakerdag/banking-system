package test.bank.account;

import bank.account.CheckingAccount;
import bank.exception.AccountFrozenException;
import bank.exception.InsufficientFundsException;
import bank.exception.InvalidAmountException;
import bank.model.Customer;
import bank.model.Transaction;
import bank.model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckingAccountTest {

    private static final LocalDate DATE = LocalDate.of(2026, 11, 26);

    private CheckingAccount account;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer("C1", "Test Customer");
        account = new CheckingAccount("ACC-1", customer);
    }

    private void assertBalance(String expected) {
        assertEquals(0, new BigDecimal(expected).compareTo(account.getBalance()));
    }

    // ---- deposit ----

    @Test
    void deposit_validAmount_increasesBalance() {
        account.deposit(new BigDecimal("100.00"), DATE);
        assertBalance("100.00");
    }

    @Test
    void deposit_validAmount_recordsDepositTransaction() {
        account.deposit(new BigDecimal("100.00"), DATE);
        Transaction last = account.getTransactions().get(0);
        assertEquals(TransactionType.DEPOSIT, last.getType());
    }

    @Test
    void deposit_zeroAmount_throwsInvalidAmountException() {
        assertThrows(InvalidAmountException.class,
                () -> account.deposit(BigDecimal.ZERO, DATE));
    }

    @Test
    void deposit_negativeAmount_throwsInvalidAmountException() {
        assertThrows(InvalidAmountException.class,
                () -> account.deposit(new BigDecimal("-5"), DATE));
    }

    @Test
    void deposit_onFrozenAccount_throwsAccountFrozenException() {
        account.freeze();
        assertThrows(AccountFrozenException.class,
                () -> account.deposit(new BigDecimal("50"), DATE));
    }

    // ---- withdraw ----

    @Test
    void withdraw_enoughBalance_decreasesBalance() {
        account.deposit(new BigDecimal("100.00"), DATE);
        account.withdraw(new BigDecimal("40.00"), DATE);
        assertBalance("60.00");
    }

    @Test
    void withdraw_withinOverdraftLimit_allowsNegativeBalance() {
        account.deposit(new BigDecimal("100.00"), DATE);
        account.withdraw(new BigDecimal("400.00"), DATE);
        assertBalance("-300.00");
    }

    @Test
    void withdraw_beyondOverdraftLimit_throwsInsufficientFundsException() {
        account.deposit(new BigDecimal("100.00"), DATE);
        assertThrows(InsufficientFundsException.class,
                () -> account.withdraw(new BigDecimal("700.00"), DATE));
    }

    @Test
    void withdraw_zeroAmount_throwsInvalidAmountException() {
        assertThrows(InvalidAmountException.class,
                () -> account.withdraw(BigDecimal.ZERO, DATE));
    }

    @Test
    void withdraw_onFrozenAccount_throwsAccountFrozenException() {
        account.deposit(new BigDecimal("100.00"), DATE);
        account.freeze();
        assertThrows(AccountFrozenException.class,
                () -> account.withdraw(new BigDecimal("10.00"), DATE));
    }
}