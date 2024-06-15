package org.rivelles.medium;

import java.util.HashMap;
import java.util.Map;

public class SubArrayDivisibleByK {
    public static int solution(int[] nums, int k) {
        var remainderCount = new HashMap<Integer, Integer>(Map.of(0, 1));
        var count = 0;
        var sum = 0;
        for (int i : nums) {
            sum += i;
            var mod = sum % k;
            if (mod < 0) {
                mod += k;
            }
            if (remainderCount.containsKey(mod)) {
                count += remainderCount.get(mod);
                remainderCount.compute(mod, (__, value) -> ++value);
                continue;
            }
            remainderCount.put(mod, 1);
        }
        return count;
    }
}
