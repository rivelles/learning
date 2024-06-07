package org.rivelles.katas;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class Multiples3And5Test {
    Multiples3And5 underTest = new Multiples3And5();

    @Test
    void solution_whenNumberIsSmallerThan3_shouldReturn0() {
        var input = 2;
        var expected = 0;

        var result = underTest.solution(input);

        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"10, 23", "20, 78"})
    void solution_shouldReturnCorrectValue(int input, int expected) {
        var result = underTest.solution(input);

        assertThat(result).isEqualTo(expected);
    }
}