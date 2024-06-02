package org.rivelles.easy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ReverseStringTest {
    ReverseString underTest = new ReverseString();
    @Test
    void reverseString_whenStringIsEmpty_shouldReturnEmpty() {
        var input = "".toCharArray();
        underTest.reverseString(input);
        var expectedValue = "".toCharArray();

        assertArrayEquals(expectedValue, input);
    }

    @Test
    void reverseString_whenOnlyOneCharacter_shouldReturnTheSame() {
        var input = "a".toCharArray();
        underTest.reverseString(input);
        var expectedValue = "a".toCharArray();

        assertArrayEquals(expectedValue, input);
    }

    @Test
    void reverseString_whenInputIsValidAndOdd_shouldReturnReversedString() {
        var input = "abc".toCharArray();
        underTest.reverseString(input);
        var expectedValue = "cba".toCharArray();

        assertArrayEquals(expectedValue, input);
    }

    @Test
    void reverseString_whenInputIsValidAndPair_shouldReturnReversedString() {
        var input = "abcd".toCharArray();
        underTest.reverseString(input);
        var expectedValue = "dcba".toCharArray();

        assertArrayEquals(expectedValue, input);
    }
}