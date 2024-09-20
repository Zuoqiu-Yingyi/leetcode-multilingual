/*
 * @lc app=leetcode.cn id=239 lang=kotlin
 *
 * [239] 滑动窗口最大值
 */

package s0.s2.s3.s9.s_0239_00

// @lc code=start
class Solution {
    fun maxSlidingWindow(nums: IntArray, k: Int): IntArray {
        var queue = mutableListOf<Int>()
        var result = mutableListOf<Int>()

        nums.forEachIndexed { i, num ->
            if (queue.isNotEmpty() && (i + 1 - k) > queue.first()) {
                queue.removeFirst()
            }
            while (queue.isNotEmpty() && nums[queue.last()] <= num) {
                queue.removeLast()
            }
            queue.addLast(i)
            if ((i + 1) >= k) {
                result.add(nums[queue.first()])
            }
        }

        return result.toIntArray()
    }
}
// @lc code=end
