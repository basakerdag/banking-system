package bank.strategy;

import bank.account.Account;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface WithdrawalRule {

    // Çekim yapılamıyorsa BankException türevi bir exception fırlatır.
    void validate(Account account, BigDecimal amount, LocalDate date);
}