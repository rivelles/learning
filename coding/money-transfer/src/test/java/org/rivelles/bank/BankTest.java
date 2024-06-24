package org.rivelles.bank;

import org.junit.jupiter.api.Test;
import org.rivelles.bank.exceptions.InvalidAmountException;
import org.rivelles.bank.exceptions.NotEnoughFundsException;
import org.rivelles.bank.exceptions.NullAmountException;
import org.rivelles.bank.exceptions.SameAccountTransferException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BankTest {
    Bank bank = new Bank();

    @Test
    void transfer_whenAccountsAreEqual_shouldThrow() {
        var account = new Account("A");

        assertThatExceptionOfType(SameAccountTransferException.class)
            .isThrownBy(() -> bank.transfer(account, account, BigDecimal.TEN));
    }

    @Test
    void transfer_whenValueIsNotPositive_shouldThrow() {
        var accountA = new Account("A");
        var accountB = new Account("B");

        assertThatExceptionOfType(InvalidAmountException.class)
            .isThrownBy(() -> bank.transfer(accountA, accountB, BigDecimal.ZERO));
    }

    @Test
    void transfer_whenValueIsNull_shouldThrow() {
        var accountA = new Account("A");
        var accountB = new Account("B");

        assertThatExceptionOfType(NullAmountException.class)
            .isThrownBy(() -> bank.transfer(accountA, accountB, null));
    }

    @Test
    void transfer_whenFromAccountDoesntHaveFounds_shouldThrow() {
        var accountA = new Account("A");
        var accountB = new Account("B");

        assertThatExceptionOfType(NotEnoughFundsException.class)
            .isThrownBy(() -> bank.transfer(accountA, accountB, BigDecimal.ONE));
    }

    @Test
    void transfer_whenInputIsValid_shouldTransfer() {
        var accountA = new Account("A");
        var accountB = new Account("B");

        accountA.credit(BigDecimal.TEN);

        bank.transfer(accountA, accountB, BigDecimal.TEN);
        assertThat(accountA.getBalance()).isEqualTo("0");
        assertThat(accountB.getBalance()).isEqualTo("10");
    }
}