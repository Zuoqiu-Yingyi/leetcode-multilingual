/*
 * @lc app=leetcode.cn id=2104 lang=kotlin
 *
 * [2104] 子数组范围和
 */

package s2.s1.s0.s4.s_2104_00

// @lc code=start
class Solution {
    fun subArrayRanges(nums: IntArray): Long {
        val n = nums.size
        var result = 0L
        for (i in 0..<(n - 1)) {
            var min = nums[i]
            var max = nums[i]
            for (j in (i + 1)..<n) {
                min = Math.min(min, nums[j])
                max = Math.max(max, nums[j])
                result += max - min
            }
        }
        return result
    }
}
// @lc code=end
