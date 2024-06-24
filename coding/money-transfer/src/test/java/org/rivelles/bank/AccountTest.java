package org.rivelles.bank;

import org.junit.jupiter.api.Test;
import org.rivelles.bank.exceptions.InvalidAmountException;
import org.rivelles.bank.exceptions.NullAmountException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    private Account account = new Account("1");

    @Test
    void credit_whenValueIsNotPositive_shouldThrow() {
        assertThatExceptionOfType(InvalidAmountException.class).isThrownBy(() -> account.credit(BigDecimal.valueOf(0)));
    }

    @Test
    void credit_whenValueIsNull_shouldThrow() {
        assertThatExceptionOfType(NullAmountException.class).isThrownBy(() -> account.credit(null));
    }

    @Test
    void debit_whenValueIsNotPositive_shouldThrow() {
        assertThatExceptionOfType(InvalidAmountException.class).isThrownBy(() -> account.debit(BigDecimal.valueOf(0)));
    }

    @Test
    void debit_whenValueIsNull_shouldThrow() {
        assertThatExceptionOfType(NullAmountException.class).isThrownBy(() -> account.debit(null));
    }

    @Test
    void debit_shouldRemoveFromBalance() {
        var newAccount = new Account("B");

        newAccount.debit(BigDecimal.TEN);

        assertThat(newAccount.getBalance()).isEqualTo("-10");
    }

    @Test
    void credit_shouldAddToBalance() {
        var newAccount = new Account("C");

        newAccount.credit(BigDecimal.TEN);

        assertThat(newAccount.getBalance()).isEqualTo("10");
    }
}