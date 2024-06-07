package org.rivelles.easy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class RemoveElementTest {
    RemoveElement underTest = new RemoveElement();

    @Test
    void removeElement_whenInputIsEmpty_shouldReturn0() {
        var input = new int[0];
        var k = 0;

        var result = underTest.removeElement(input, k);
        var expectedArray = new int[0];
        var expectedResult = 0;

        assertArrayEquals(expectedArray, input);
        assertEquals(expectedResult, result);
    }

    @Test
    void removeElement_whenInputSizeIsOneAndKIsDifferent_shouldReturn0() {
        var input = new int[]{1};
        var k = 2;

        var result = underTest.removeElement(input, k);
        var expectedArray = new int[]{1};
        var expectedResult = 1;

        assertArrayEquals(expectedArray, input);
        assertEquals(expectedResult, result);
    }

    @Test
    void removeElement_whenRemovesAll_shouldReturn0() {
        var input = new int[]{3,3};
        var k = 3;

        var result = underTest.removeElement(input, k);
        var expectedArray = new int[]{};
        var expectedResult = 0;

        assertArrayEquals(expectedArray, Arrays.stream(input).limit(expectedArray.length).toArray());
        assertEquals(expectedResult, result);
    }

    @Test
    void removeElement_whenInputSizeIsOneAndKIsEqual_shouldReturn1() {
        var input = new int[]{1};
        var k = 1;

        var result = underTest.removeElement(input, k);
        var expectedResult = 0;

        assertEquals(expectedResult, result);
    }

    @Test
    void removeElement_whenInputMatches_shouldReturnExpectedValue() {
        var input = new int[]{3,2,2,3};
        var k = 3;

        var result = underTest.removeElement(input, k);
        var expectedArray = new int[]{2,2};
        var expectedResult = 2;

        assertArrayEquals(expectedArray, Arrays.stream(input).limit(expectedArray.length).toArray());
        assertEquals(expectedResult, result);
    }
}