/*
 * @lc app=leetcode.cn id=1 lang=java
 *
 * [1] 两数之和
 */

package _0._0._0._1;

import java.util.HashMap;
import java.util.Map;

// @lc code=start
class Solution {
    public int[] twoSum(int[] nums, int target) {
        this.t();
        final Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; ; ++i) {
            final int num = nums[i];
            final int diff = target - num;
            final Integer j = map.get(diff);
            if (j != null) {
                return new int[] {j, i};
            }
            map.put(num, i);
        }
    }

    protected void t() {}
}

// @lc code=end

public class s_0001_01 extends Solution {}
