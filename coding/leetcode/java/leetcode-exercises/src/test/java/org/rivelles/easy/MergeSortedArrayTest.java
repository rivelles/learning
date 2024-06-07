package org.rivelles.easy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MergeSortedArrayTest {
    private MergeSortedArray underTest = new MergeSortedArray();

    @Test
    void merge_whenArraysAreEmpty_shouldSetEmpty() {
        var input1 = new int[]{};
        var input2 = new int[]{};
        var m = 0;
        var n = input2.length;

        underTest.merge(input1, m, input2, n);
        var expected = new int[]{};
        assertArrayEquals(expected, input1);
    }

    @Test
    void merge_whenInputTwoIsEmpty_shouldSetSameInput() {
        var input1 = new int[]{1, 2, 3};
        var input2 = new int[]{};
        var m = 3;
        var n = input2.length;

        underTest.merge(input1, m, input2, n);
        var expected = new int[]{1, 2, 3};
        assertArrayEquals(expected, input1);
    }

    @Test
    void merge_whenInput1IsEmpty_shouldSetFromInput2() {
        var input1 = new int[]{0, 0, 0};
        var input2 = new int[]{1, 2, 3};
        var m = 0;
        var n = input2.length;

        underTest.merge(input1, m, input2, n);
        var expected = new int[]{1, 2, 3};
        assertArrayEquals(expected, input1);
    }

    @Test
    void merge_whenInput1And2AreDifferentAndReverse_shouldOrder() {
        var input1 = new int[]{4, 5, 6, 0, 0, 0};
        var input2 = new int[]{1, 2, 3};
        var m = 3;
        var n = input2.length;

        underTest.merge(input1, m, input2, n);
        var expected = new int[]{1, 2, 3, 4, 5, 6};
        assertArrayEquals(expected, input1);
    }

    @Test
    void merge_whenInput1And2AreDifferent_shouldOrder() {
        var input1 = new int[]{1, 2, 3, 0, 0, 0};
        var input2 = new int[]{4, 5, 6};
        var m = 3;
        var n = input2.length;

        underTest.merge(input1, m, input2, n);
        var expected = new int[]{1, 2, 3, 4, 5, 6};
        assertArrayEquals(expected, input1);
    }

    @Test
    void merge_whenInput1And2AreDifferentAndRepeatedNumbers_shouldOrder() {
        var input1 = new int[]{1, 2, 3, 0, 0, 0};
        var input2 = new int[]{2, 5, 6};
        var m = 3;
        var n = input2.length;

        underTest.merge(input1, m, input2, n);
        var expected = new int[]{1, 2, 2, 3, 5, 6};
        assertArrayEquals(expected, input1);
    }

    @Test
    void merge_whenInput1And2AreDifferentAndInput1HasOneValue_shouldOrder() {
        var input1 = new int[]{4,0,0,0,0,0};
        var input2 = new int[]{1,2,3,5,6};
        var m = 1;
        var n = input2.length;

        underTest.merge(input1, m, input2, n);
        var expected = new int[]{1,2,3,4,5,6};
        assertArrayEquals(expected, input1);
    }
}