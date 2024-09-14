/*
 * @lc app=leetcode.cn id=2104 lang=kotlin
 *
 * [2104] 子数组范围和
 */

package s2.s1.s0.s4.s_2104_01

// @lc code=start
class Solution {
    fun subArrayRanges(nums: IntArray): Long {
        val max = this.f(nums)
        val min = this.f(nums.map { -it }.toIntArray())
        return max + min
    }

    private fun f(nums: IntArray): Long {
        val n = nums.size
        var stack = mutableListOf<Int>()
        var left = IntArray(n)
        var right = IntArray(n)

        nums.forEachIndexed { i, num ->
            while (stack.isNotEmpty() && nums[stack.last()] <= num) {
                stack.removeLast()
            }
            left[i] = stack.lastOrNull() ?: -1
            stack.add(i)
        }

        stack.clear()
        nums.reversed().forEachIndexed { i, num ->
            val j = n - i - 1
            while (stack.isNotEmpty() && nums[stack.last()] < num) {
                stack.removeLast()
            }
            right[j] = stack.lastOrNull() ?: n
            stack.add(j)
        }

        return nums.foldIndexed(0L) { i, acc, num ->
            acc + (i - left[i]).toLong() * (right[i] - i).toLong() * num
        }
    }
}
// @lc code=end
