package test.bank.account;

import bank.account.CheckingAccount;
import bank.model.AccountStatus;
import bank.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountFreezeTest {

    private static final LocalDate DATE = LocalDate.of(2026, 11, 28);

    private CheckingAccount account;

    @BeforeEach
    void setUp() {
        account = new CheckingAccount("CHK-3", new Customer("C4", "Freeze Customer"));
    }

    @Test
    void newAccount_isActive() {
        assertEquals(AccountStatus.ACTIVE, account.getStatus());
    }

    @Test
    void freeze_activeAccount_changesStatusToFrozen() {
        account.freeze();
        assertEquals(AccountStatus.FROZEN, account.getStatus());
    }

    @Test
    void unfreeze_frozenAccount_changesStatusToActive() {
        account.freeze();
        account.unfreeze();
        assertEquals(AccountStatus.ACTIVE, account.getStatus());
    }

    @Test
    void deposit_afterUnfreeze_succeeds() {
        account.freeze();
        account.unfreeze();
        account.deposit(new BigDecimal("50.00"), DATE);
        assertEquals(0, new BigDecimal("50.00").compareTo(account.getBalance()));
    }
}