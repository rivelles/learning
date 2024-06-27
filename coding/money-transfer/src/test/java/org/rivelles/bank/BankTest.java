package org.rivelles.bank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.rivelles.bank.exceptions.InvalidTransactionException;
import org.rivelles.bank.exceptions.NotEnoughFundsException;

import java.math.BigDecimal;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    Bank underTest = new Bank();

    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void transfer_whenAmountIsNotPositive_shouldThrow(long value) {
        var input = BigDecimal.valueOf(value);
        var accountA = new Account("A");
        var accountB = new Account("B");

        assertThatExceptionOfType(InvalidTransactionException.class).isThrownBy(() -> underTest.transfer(accountA, accountB, input));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void transfer_whenAccountsAreNull_shouldThrow(boolean first) {
        var input = BigDecimal.TEN;
        var account = new Account("A");
        if (first) {
            assertThatExceptionOfType(InvalidTransactionException.class).isThrownBy(() -> underTest.transfer(null, account, input));
        }
        else {
            assertThatExceptionOfType(InvalidTransactionException.class).isThrownBy(() -> underTest.transfer(account, null, input));
        }
    }

    @Test
    void transfer_whenTransferToTheSameAccount_shouldThrow() {
        var input = BigDecimal.TEN;
        var account = new Account("A");

        assertThatExceptionOfType(InvalidTransactionException.class).isThrownBy(() -> underTest.transfer(account, account, input));
    }

    @Test
    void transfer_whenAccountDoesnHaveFunds_shouldThrow() {
        var input = BigDecimal.TEN;
        var accountA = new Account("A");
        var accountB = new Account("B");

        assertThatExceptionOfType(NotEnoughFundsException.class).isThrownBy(() -> underTest.transfer(accountA, accountB, input));
    }

    @Test
    void transfer_shouldTransfer() {
        var input = BigDecimal.TEN;
        var accountA = new Account("A");
        var accountB = new Account("B");

        accountA.credit(BigDecimal.TEN);
        underTest.transfer(accountA, accountB, input);

        assertThat(accountA.getBalance()).isEqualTo(BigDecimal.ZERO);
        assertThat(accountB.getBalance()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void synchronization() throws InterruptedException {
        var accountA = new Account("A");
        var accountB = new Account("B");

        accountA.credit(BigDecimal.valueOf(100_000L));
        accountB.credit(BigDecimal.valueOf(100_000L));
        IntStream.range(0, 50_000).forEach(__ -> {
            new Thread(() -> {
                underTest.transferNotThreadSafe(accountA, accountB, BigDecimal.ONE);
            }).start();
        });

        Thread.sleep(5000L);

        assertThat(accountA.getBalance()).isEqualTo(BigDecimal.valueOf(50_000L));
        assertThat(accountB.getBalance()).isEqualTo(BigDecimal.valueOf(150_000L));
    }
}