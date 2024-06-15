package org.rivelles.medium;

public class RemoveDuplicates {
    public int removeDuplicates(int[] nums) {
        int count = 0;

        for (int i=0; i<nums.length; i++) {
            if (i == 0 || i == 1) {
                count++;
                continue;
            }

            if (nums[count-2] != nums[i]) {
                nums[count] = nums[i];
                count++;
            }
        }
        return count;
    }
}
