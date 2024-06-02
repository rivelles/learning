package org.rivelles.easy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TwoSumTest {
    TwoSum underTest = new TwoSum();
    @Test
    void twoSum_whenThereAreOnlyTwoNumbers_shouldReturnThem() {
        var inputArray = new int[]{1, 2};
        var target = 3;
        var expected = new int[]{0, 1};

        var response = underTest.twoSum(inputArray, target);

        assertArrayEquals(expected, response);
    }

    @Test
    void twoSum_whenThereAreEqualNumbers_shouldReturnCorrectSolution() {
        var inputArray = new int[]{1, 3, 3, 4};
        var target = 6;
        var expected = new int[]{1, 2};

        var response = underTest.twoSum(inputArray, target);

        assertArrayEquals(expected, response);
    }

    @Test
    void twoSum_shouldReturnCorrectSolution() {
        var inputArray = new int[]{3,2,4};
        var target = 6;
        var expected = new int[]{1, 2};

        var response = underTest.twoSum(inputArray, target);

        assertArrayEquals(expected, response);
    }
}