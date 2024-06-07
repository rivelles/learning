package org.rivelles.easy;

public class RemoveElement {
    public int removeElement(int[] nums, int val) {
        if (nums.length == 0) return 0;
        if (nums.length == 1) {
            if (nums[0] != val) return 1;
            return 0;
        }

        int counter = 0;
        int reverseCounter = nums.length-1;
        for (int i=0; i<nums.length; i++) {
            if (nums[i] == val) {
                while (nums[reverseCounter] == val) {
                    if (counter >= reverseCounter) break;
                    reverseCounter--;
                }
                if (counter >= reverseCounter) break;
                var temp = nums[reverseCounter];
                nums[reverseCounter] = nums[i];
                nums[i] = temp;
                counter++;
                reverseCounter--;
                continue;
            }
            counter++;
        }
        return counter;
    }
}
