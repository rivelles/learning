package org.rivelles.katas;

public class BinaryAddition {
    public static String binaryAddition(int a, int b){
        var sum = a+b;
        if (sum == 1) return "1";

        var binary = new StringBuilder();
        while (sum > 0) {
            binary.insert(0, sum % 2);
            sum = sum/2;
        }
        return binary.toString();
    }
}
