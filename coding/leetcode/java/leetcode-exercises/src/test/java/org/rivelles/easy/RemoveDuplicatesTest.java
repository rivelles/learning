package org.rivelles.easy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RemoveDuplicatesTest {
    RemoveDuplicates underTest = new RemoveDuplicates();

    @Test
    void removeDuplicates_whenArrayIsEmpty_shouldReturn0() {
        var input = new int[0];
        var expected = 0;

        var result = underTest.removeDuplicates(input);

        assertEquals(expected, result);
    }

    @Test
    void removeDuplicates_whenArrayHasOnlyOneElement_shouldReturn1() {
        var input = new int[]{1};
        var expected = 1;

        var result = underTest.removeDuplicates(input);

        assertEquals(expected, result);
    }

    @Test
    void removeDuplicates_whenArrayHasOnlyDuplicates_shouldReturn1() {
        var input = new int[]{1,1,1,1,1,1,1,1,1,1,1};
        var expected = 1;

        var result = underTest.removeDuplicates(input);

        assertEquals(expected, result);
    }

    @Test
    void removeDuplicates_whenArrayContainsMultipleElements_shouldReturnUniqueValues() {
        var input = new int[]{1,2,3,4,5};
        var expected = 5;

        var result = underTest.removeDuplicates(input);

        assertEquals(expected, result);
    }

    @Test
    void removeDuplicates_whenArrayContainsMultipleRepeatedElements_shouldReturnUniqueValues() {
        var input = new int[]{0,0,1,1,1,2,2,3,3,4};
        var expected = 5;

        var result = underTest.removeDuplicates(input);

        assertEquals(expected, result);
    }
}