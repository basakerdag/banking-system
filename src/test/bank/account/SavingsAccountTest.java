package test.bank.account;

import bank.account.SavingsAccount;
import bank.exception.InsufficientFundsException;
import bank.exception.WithdrawalLimitExceededException;
import bank.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SavingsAccountTest {

    private static final LocalDate NOVEMBER_DATE = LocalDate.of(2026, 11, 20);
    private static final LocalDate DECEMBER_DATE = LocalDate.of(2026, 12, 1);

    private SavingsAccount account;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer("C2", "Savings Customer");
        account = new SavingsAccount("SAV-1", customer);
        account.deposit(new BigDecimal("1000.00"), NOVEMBER_DATE);
    }

    private void assertBalance(String expected) {
        assertEquals(0, new BigDecimal(expected).compareTo(account.getBalance()));
    }

    private void withdrawThreeTimesInNovember() {
        for (int i = 0; i < 3; i++) {
            account.withdraw(new BigDecimal("10.00"), NOVEMBER_DATE);
        }
    }

    @Test
    void withdraw_enoughBalance_decreasesBalance() {
        account.withdraw(new BigDecimal("200.00"), NOVEMBER_DATE);
        assertBalance("800.00");
    }

    @Test
    void withdraw_moreThanBalance_throwsInsufficientFundsException() {
        assertThrows(InsufficientFundsException.class,
                () -> account.withdraw(new BigDecimal("1500.00"), NOVEMBER_DATE));
    }

    @Test
    void withdraw_fourthTimeInSameMonth_throwsWithdrawalLimitExceededException() {
        withdrawThreeTimesInNovember();
        assertThrows(WithdrawalLimitExceededException.class,
                () -> account.withdraw(new BigDecimal("10.00"), NOVEMBER_DATE));
    }

    @Test
    void withdraw_limitExceeded_keepsBalanceUnchanged() {
        withdrawThreeTimesInNovember();
        try {
            account.withdraw(new BigDecimal("10.00"), NOVEMBER_DATE);
        } catch (WithdrawalLimitExceededException expected) {
            // beklenen hata, bakiyeye bakmak için yakalıyoruz
        }
        assertBalance("970.00");
    }

    @Test
    void withdraw_fourthTimeButInNextMonth_succeeds() {
        withdrawThreeTimesInNovember();
        account.withdraw(new BigDecimal("10.00"), DECEMBER_DATE);
        assertBalance("960.00");
    }
}