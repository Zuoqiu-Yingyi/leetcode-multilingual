/*
 * @lc app=leetcode.cn id=713 lang=kotlin
 *
 * [713] 乘积小于 K 的子数组
 */

package s0.s7.s1.s3.s_0713_00

// @lc code=start
class Solution {
    fun numSubarrayProductLessThanK(nums: IntArray, k: Int): Int {
        var left = 0
        var count = 0
        var product = 1
        nums.forEachIndexed { right, num ->
            product *= num
            while (product >= k && left <= right) {
                product /= nums[left++]
            }
            count += right - left + 1
        }
        return count
    }
}
// @lc code=end
