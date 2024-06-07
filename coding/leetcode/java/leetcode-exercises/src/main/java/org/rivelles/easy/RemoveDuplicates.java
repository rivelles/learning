package org.rivelles.easy;

public class RemoveDuplicates {
    public int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        if (nums.length == 1 || nums[0] == nums[nums.length-1]) return 1;
        if (nums.length == 2) {
            if (nums[0] == nums[1]) return 1;
            return 2;
        }

        int pointer1 = 0;
        int pointer2 = 1;
        while (pointer2 < nums.length) {
            if (nums[pointer1] < nums[pointer2]) {
                if (pointer2 - pointer1 == 1) {
                    pointer1++;
                    pointer2++;
                    continue;
                }
                pointer1++;
                nums[pointer1] = nums[pointer2];
            }
            pointer2++;
        }
        return pointer1+1;
    }
}
