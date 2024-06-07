package org.rivelles.katas;

public class XO {

    private static final char X = 'x';
    private static final char O = 'o';

    public static boolean getXO (String str) {
        if (str == null || str.isEmpty()) return true;

        var accumulator = 0;
        for (char c : str.toCharArray()) {
            var lowercaseChar = Character.toLowerCase(c);
            switch (lowercaseChar) {
                case X -> accumulator++;
                case O -> accumulator--;
            }
        }
        return accumulator == 0;
    }
}
