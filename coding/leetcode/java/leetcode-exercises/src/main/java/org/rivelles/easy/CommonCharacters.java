package org.rivelles.easy;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;

public class CommonCharacters {
    public List<String> commonChars(String[] words) {
        if (words.length == 0) return emptyList();
        if (words.length == 1) {
            var charList = new ArrayList<String>();
            for (Character i : words[0].toCharArray()) {
                charList.add(i.toString());
            }
            return charList;
        }
        // Two hashmaps, one for letter and set of strings, other for letter and min frequency
        var wordsPerCharacter = new HashMap<Character, Set<String>>();
        var minFrequencyPerCharacter = new HashMap<Character, Integer>();
        // For each letter, include string in the appropriate set and update min frequency
        for (String word : words) {
            var frequencies = new HashMap<Character, Integer>();
            for (char c : word.toCharArray()) {
                wordsPerCharacter.computeIfPresent(c, (character, strings) -> {
                    strings.add(word);
                    return strings;
                });
                wordsPerCharacter.putIfAbsent(c, new HashSet<>(List.of(word)));
                frequencies.computeIfPresent(c, (character, integer) -> integer+1);
                frequencies.putIfAbsent(c, 1);
            }
            for (char c : word.toCharArray()) {
                minFrequencyPerCharacter.computeIfPresent(c, (character, integer) -> Math.min(integer, frequencies.get(c)));
                minFrequencyPerCharacter.putIfAbsent(c, frequencies.get(c));
            }
        }
        // At the end, iterate over first map, if there are N words in the set, repeat for the min frequency
        var returnList = new ArrayList<String>();
        wordsPerCharacter.forEach((key, value) -> {
            if (value.size() == Arrays.stream(words).collect(Collectors.toSet()).size()) {
                IntStream.range(0, minFrequencyPerCharacter.get(key)).forEach(__ -> returnList.add(key.toString()));
            }
        });

        return returnList;
    }
}
