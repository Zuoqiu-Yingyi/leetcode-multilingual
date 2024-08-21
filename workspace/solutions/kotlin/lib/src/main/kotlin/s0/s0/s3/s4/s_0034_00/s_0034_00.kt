/*
 * @lc app=leetcode.cn id=34 lang=kotlin
 *
 * [34] 在排序数组中查找元素的第一个和最后一个位置
 */

package s0.s0.s3.s4.s_0034_00

// @lc code=start
class Solution {
    fun searchRange(nums: IntArray, target: Int): IntArray {
        val left = this.search(nums, target)
        val right = this.search(nums, target + 1)
        return if (left == right) intArrayOf(-1, -1) else intArrayOf(left, right - 1)
    }

    private fun search(nums: IntArray, target: Int): Int {
        var left = 0
        var right = nums.size
        while (left < right) {
            val middle = (left + right) / 2
            if (nums[middle] < target) {
                left = middle + 1
            } else {
                right = middle
            }
        }
        return left
    }
}
// @lc code=end
