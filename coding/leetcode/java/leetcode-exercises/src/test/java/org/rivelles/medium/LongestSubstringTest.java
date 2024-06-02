package org.rivelles.medium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LongestSubstringTest {
    LongestSubstring underTest = new LongestSubstring();

    @Test
    void longestSubstring_whenInputIsEmpty_shouldReturn0() {
        var input = "";
        var result = underTest.lengthOfLongestSubstring(input);
        var expected = 0;

        assertEquals(expected, result);
    }

    @Test
    void longestSubstring_whenInputHasSizeOne_shouldReturn1() {
        var input = "b";
        var result = underTest.lengthOfLongestSubstring(input);
        var expected = 1;

        assertEquals(expected, result);
    }

    @Test
    void longestSubstring_whenInputHasOnlyOneRepeatedCharacter_shouldReturn1() {
        var input = "bbbbbbbbb";
        var result = underTest.lengthOfLongestSubstring(input);
        var expected = 1;

        assertEquals(expected, result);
    }

    @Test
    void longestSubstring_whenInputHasOnlyDifferentCharacters_shouldReturnCorrectAnswer() {
        var input = "abcde";
        var result = underTest.lengthOfLongestSubstring(input);
        var expected = 5;

        assertEquals(expected, result);
    }

    @Test
    void longestSubstring_whenInputContainsRepeatedCharactersInBeginning_shouldReturnCorrectAnswer() {
        var input = "bbbbbbabc";
        var result = underTest.lengthOfLongestSubstring(input);
        var expected = 3;

        assertEquals(expected, result);
    }

    @Test
    void longestSubstring_whenInputContainsRepeatedCharactersInEnd_shouldReturnCorrectAnswer() {
        var input = "abcbbbbbb";
        var result = underTest.lengthOfLongestSubstring(input);
        var expected = 3;

        assertEquals(expected, result);
    }

    @Test
    void longestSubstring_whenInputContainsNoRepeatedCharactersInMiddle_shouldReturnCorrectAnswer() {
        var input = "aabaab!bb";
        var result = underTest.lengthOfLongestSubstring(input);
        var expected = 3;

        assertEquals(expected, result);
    }

    @Test
    void longestSubstring_whenInputContainsMultipleNoRepeatedCharacters_shouldReturnCorrectAnswer() {
        var input = "abcdefbbbbbbbbbghijklmn";
        var result = underTest.lengthOfLongestSubstring(input);
        var expected = 9;

        assertEquals(expected, result);
    }
}