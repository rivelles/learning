package org.rivelles.katas;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class DigPow {
    public static long digPow(int n, int p) {
        if (n == 1) return 1;
        var i = new AtomicInteger(0);
        var nString = String.valueOf(n);
        var sum = IntStream.range(p, nString.length() + p)
            .map(operand -> powerDigit(Character.getNumericValue(nString.charAt(i.getAndIncrement())), operand))
            .sum();
        double returnedNumber = (double) sum / n;
        return returnedNumber % 1 == 0 ? (long) returnedNumber : -1;
    }

    private static int powerDigit(int digit, int operand) {
        var i1 = Math.pow(digit, operand);
        return (int) i1;
    }
}
