/*
 * @lc app=leetcode.cn id=962 lang=kotlin
 *
 * [962] 最大宽度坡
 */

package s0.s9.s6.s2.s_0962_00

import kotlin.collections.mutableListOf

// @lc code=start
class Solution {
    fun maxWidthRamp(nums: IntArray): Int {
        val n = nums.size
        var stack = mutableListOf<Int>()

        nums.forEachIndexed { i, num ->
            if (stack.isEmpty() || nums[stack.last()] > num) {
                stack.add(i)
            }
        }

        var result = 0
        nums.reversed().forEachIndexed { i, num ->
            val j = n - i - 1
            while (stack.isNotEmpty() && nums[stack.last()] <= num) {
                result = maxOf(result, j - stack.removeLast())
            }
            if (stack.isEmpty()) {
                return result
            }
        }
        return result
    }
}
// @lc code=end
