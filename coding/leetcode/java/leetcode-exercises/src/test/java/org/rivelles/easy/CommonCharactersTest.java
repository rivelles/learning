package org.rivelles.easy;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonCharactersTest {
    CommonCharacters underTest = new CommonCharacters();

    @Test
    void commonChars_whenStringsAreEmpty_shouldReturnEmpty() {
        var input = new String[] {};

        var result = underTest.commonChars(input);
        var expected = List.of();

        assertEquals(expected, result);
    }

    @Test
    void commonChars_whenThereIsOnlyOneInput_shouldReturnItsCharacters() {
        var input = new String[] {"bella"};

        var result = underTest.commonChars(input);
        var expected = List.of("b", "e", "l", "l", "a");

        assertEquals(expected, result);
    }

    @Test
    void commonChars_whenThereAreMultipleWords_shouldReturnMainCharacters() {
        var input = new String[] {"bella", "label", "roller"};

        var result = underTest.commonChars(input);
        var expected = List.of("e", "l", "l");

        assertEquals(expected, result);
    }

    @Test
    void commonChars_whenThereAreMultipleWordsWithDifferentFrequencies_shouldReturnMainCharacters() {
        var input = new String[] {"l", "ll", "l"};

        var result = underTest.commonChars(input);
        var expected = List.of("l");

        assertEquals(expected, result);
    }
}