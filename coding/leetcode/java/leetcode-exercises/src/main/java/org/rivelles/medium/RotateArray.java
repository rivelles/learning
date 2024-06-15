package org.rivelles.medium;

public class RotateArray {
    public void rotate(int[] nums, int k) {
        if (k == 0 || (k > nums.length && nums.length % k == 0)) return;

        int[] aux = new int[nums.length];
        for (int i=0; i<nums.length; i++) {
            int newPosition = (i+k)%nums.length;
            aux[newPosition] = nums[i];
        }
        System.arraycopy(aux, 0, nums, 0, aux.length);
    }
}
