package test.bank.account;

import bank.account.CheckingAccount;
import bank.account.SavingsAccount;
import bank.exception.AccountFrozenException;
import bank.model.Customer;
import bank.model.Transaction;
import bank.model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountInterestTest {

    private static final LocalDate DATE = LocalDate.of(2026, 11, 30);

    private CheckingAccount checking;
    private SavingsAccount savings;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer("C3", "Interest Customer");
        checking = new CheckingAccount("CHK-2", customer);
        savings = new SavingsAccount("SAV-2", customer);
    }

    private void assertAmount(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual));
    }

    @Test
    void applyMonthlyInterest_checkingAccount_usesLowRate() {
        checking.deposit(new BigDecimal("1000.00"), DATE);
        BigDecimal interest = checking.applyMonthlyInterest(DATE);
        assertAmount("1.00", interest);
    }

    @Test
    void applyMonthlyInterest_savingsAccount_usesHigherRate() {
        savings.deposit(new BigDecimal("1000.00"), DATE);
        BigDecimal interest = savings.applyMonthlyInterest(DATE);
        assertAmount("5.00", interest);
    }

    @Test
    void applyMonthlyInterest_positiveBalance_addsInterestToBalance() {
        savings.deposit(new BigDecimal("1000.00"), DATE);
        savings.applyMonthlyInterest(DATE);
        assertAmount("1005.00", savings.getBalance());
    }

    @Test
    void applyMonthlyInterest_positiveBalance_recordsInterestTransaction() {
        savings.deposit(new BigDecimal("1000.00"), DATE);
        savings.applyMonthlyInterest(DATE);
        Transaction last = savings.getTransactions().get(1);
        assertEquals(TransactionType.INTEREST, last.getType());
    }

    @Test
    void applyMonthlyInterest_zeroBalance_returnsZeroAndRecordsNothing() {
        BigDecimal interest = savings.applyMonthlyInterest(DATE);
        assertAmount("0", interest);
        assertEquals(0, savings.getTransactions().size());
    }

    @Test
    void applyMonthlyInterest_negativeBalance_earnsNoInterest() {
        checking.deposit(new BigDecimal("100.00"), DATE);
        checking.withdraw(new BigDecimal("400.00"), DATE);
        BigDecimal interest = checking.applyMonthlyInterest(DATE);
        assertAmount("0", interest);
        assertAmount("-300.00", checking.getBalance());
    }

    @Test
    void applyMonthlyInterest_onFrozenAccount_throwsAccountFrozenException() {
        savings.deposit(new BigDecimal("1000.00"), DATE);
        savings.freeze();
        assertThrows(AccountFrozenException.class,
                () -> savings.applyMonthlyInterest(DATE));
    }
}