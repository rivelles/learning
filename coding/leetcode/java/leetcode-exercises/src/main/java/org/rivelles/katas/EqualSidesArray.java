package org.rivelles.katas;

public class EqualSidesArray {
    public static int findEvenIndex(int[] arr) {
        if (arr.length == 0) return 0;
        int[] rightSide = new int[arr.length];
        int accRight = 0;
        for (int i=arr.length-1; i>=0; i--) {
            rightSide[i] = accRight;
            accRight = accRight + arr[i];
        }
        int accLeft = 0;
        for (int i=0; i<arr.length; i++) {
            if (accLeft == rightSide[i]) return i;
            accLeft += arr[i];
        }
        return -1;
    }
}
