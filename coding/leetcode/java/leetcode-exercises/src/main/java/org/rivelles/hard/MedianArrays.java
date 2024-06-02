package org.rivelles.hard;

public class MedianArrays {
    /*
    Time complexity:
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1 == null || nums1.length == 0) {
            if (nums2 == null || nums2.length == 0) return 0.0;
            if (nums2.length == 1) return nums2[0];
        }
        if (nums2 == null || nums2.length == 0) {
            if (nums1.length == 1) return nums1[0];
        }
        if (nums1.length == 1 && nums2.length == 1) return (double) (nums1[0] + nums2[0]) /2;

        int totalLength = nums1.length + nums2.length;
        int counter1 = 0;
        int counter2 = 0;
        int prev = 0;
        while(true) {
            int value;
            if (counter1 > nums1.length-1) value = nums2[counter2];
            else if (counter2 > nums2.length-1) value = nums1[counter1];
            else value = Math.min(nums1[counter1], nums2[counter2]);
            if (counter1 + counter2 == totalLength/2) {
                if (totalLength % 2 != 0) return value;
                return (double) (prev + value) /2;
            }
            prev = value;
            if (counter1 > nums1.length-1) counter2++;
            else if (counter2 > nums2.length-1) counter1++;
            else if (nums1[counter1] < nums2[counter2]) counter1++;
            else counter2++;
        }
    }
}
