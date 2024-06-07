package org.rivelles.easy;

public class MergeSortedArray {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        if (n == 0) return;
        if (m == 0) {
            if (n >= 0) System.arraycopy(nums2, 0, nums1, 0, n);
            return;
        }

        int counter1 = 0;
        int counter2 = 0;
        int[] array = new int[nums1.length];
        for (int i=0; i<nums1.length; i++) {
            if (counter1 > m-1 || counter2 > n-1) {
                if (counter2 == nums2.length) {
                    array[i] = nums1[counter1];
                    counter1++;
                    continue;
                }
                array[i] = nums2[counter2];
                counter2++;
                continue;
            }
            array[i] = Math.min(nums1[counter1], nums2[counter2]);
            if (array[i] == nums1[counter1]) counter1++;
            else counter2++;
        }
        System.arraycopy(array, 0, nums1, 0, nums1.length);
    }
}
