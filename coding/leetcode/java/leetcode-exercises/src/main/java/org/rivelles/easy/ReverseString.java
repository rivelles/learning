package org.rivelles.easy;

public class ReverseString {
    /*
    Time complexity: Linear O(n/2) - always iterates over half of the array
    Space complexity: Constant O(1) - only uses a temp variable
     */
    public void reverseString(char[] s) {
        if (s == null || s.length == 0) return;

        for (int i=0; i<s.length/2; i++) {
            revertChar(s, i);
        }
    }

    private void revertChar(char[] s, int position) {
        char temp = s[position];
        s[position] = s[s.length-1- position];
        s[s.length - 1 - position] = temp;
    }
}
