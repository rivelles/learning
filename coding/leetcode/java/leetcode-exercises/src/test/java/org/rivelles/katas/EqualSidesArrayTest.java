package org.rivelles.katas;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EqualSidesArrayTest {
    @Test
    void find_whenArrayIsEmpty_shouldReturn0() {
        assertThat(EqualSidesArray.findEvenIndex(new int[]{})).isEqualTo(0);
    }

    @Test
    void find_whenThereIsNoSolution_shouldReturnMinus1() {
        assertThat(EqualSidesArray.findEvenIndex(new int[]{1,2})).isEqualTo(-1);
    }

    @Test
    void find_whenThereIsASolution_shouldReturnResponse() {
        assertThat(EqualSidesArray.findEvenIndex(new int[]{1,2,3,4,3,2,1})).isEqualTo(3);
    }
}