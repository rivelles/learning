package org.rivelles.easy;

import java.util.HashMap;

public class TwoSum {
    /*
    Time complexity: Linear O(n) - iterates over array once and lookup HashMap in O(1)
    Space complexity: O(n) - uses a hashmap with the same size as the input
     */
    public int[] twoSum(int[] nums, int target) {
        if (nums.length == 2) {
            return new int[]{0, 1};
        }
        var indexByValue = new HashMap<Integer, Integer>();
        for (int i=0; i<nums.length; i++) {
            indexByValue.put(nums[i], i);
        }
        for (int i=0; i<nums.length; i++) {
            var indexIfMatch = indexByValue.get(target - nums[i]);
            if (indexIfMatch != null && indexIfMatch != i) {
                return new int[]{i, indexIfMatch};
            }
        }
        return null;
    }
}
