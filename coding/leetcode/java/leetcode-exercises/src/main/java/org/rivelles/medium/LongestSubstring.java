package org.rivelles.medium;

import java.util.HashMap;

public class LongestSubstring {
    /*
    Time complexity: O(n) - Iterates over the String once and operations in HashMap are O(1)
    Space complexity: O(n) - Creates a map with the same size as the input length
     */
    public int lengthOfLongestSubstring(String s) {
        if (s.isEmpty()) return 0;
        if (s.length() == 1) return 1;

        var maxSequence = 1;
        var sequenceStartIndex = 0;
        var charByIndex = new HashMap<Character, Integer>();
        charByIndex.put(s.charAt(0), 0);
        for (int i=1; i<s.length(); i++) {
            var currentChar = s.charAt(i);
            if (!charByIndex.containsKey(currentChar) || charByIndex.get(currentChar) < sequenceStartIndex) {
                charByIndex.put(currentChar, i);
                maxSequence = Math.max(maxSequence, i+1-sequenceStartIndex);
                continue;
            }
            sequenceStartIndex = charByIndex.get(currentChar)+1;
            charByIndex.put(currentChar, i);
            maxSequence = Math.max(maxSequence, i-sequenceStartIndex);
        }

        return maxSequence;
    }
}
