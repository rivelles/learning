package org.rivelles.hard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MedianArraysTest {
    private MedianArrays underTest = new MedianArrays();
    @Test
    void findMedian_whenThereAreNoValues_shouldReturn0() {
        var input1 = new int[] {};
        var input2 = new int[] {};
        var expectedResult = 0.0;

        var result = underTest.findMedianSortedArrays(input1, input2);

        assertEquals(expectedResult, result);
    }
    @Test
    void findMedian_whenThereIsOnlyOneValue_shouldReturnIt() {
        var input1 = new int[] {1};
        var input2 = new int[] {};
        var expectedResult = 1.0;

        var result = underTest.findMedianSortedArrays(input1, input2);

        assertEquals(expectedResult, result);
    }

    @Test
    void findMedian_whenThereIsOneValueEach_shouldReturnDivisionBy2() {
        var input1 = new int[] {1};
        var input2 = new int[] {2};
        var expectedResult = 1.5;

        var result = underTest.findMedianSortedArrays(input1, input2);

        assertEquals(expectedResult, result);
    }

    @Test
    void findMedian_whenSizeIsOdd_shouldReturnMedian() {
        var input1 = new int[] {1, 2};
        var input2 = new int[] {3};
        var expectedResult = 2.0;

        var result = underTest.findMedianSortedArrays(input1, input2);

        assertEquals(expectedResult, result);
    }

    @Test
    void findMedian_whenSizeIsPair_shouldReturnMedian() {
        var input1 = new int[] {1, 2};
        var input2 = new int[] {3, 4};
        var expectedResult = 2.5;

        var result = underTest.findMedianSortedArrays(input1, input2);

        assertEquals(expectedResult, result);
    }

    @Test
    void findMedian_whenOnlyOneArrayIsFilled_shouldReturnMedian() {
        var input1 = new int[] {1, 2, 3, 4, 5, 6};
        var input2 = new int[] {};
        var expectedResult = 3.5;

        var result = underTest.findMedianSortedArrays(input1, input2);

        assertEquals(expectedResult, result);
    }
}