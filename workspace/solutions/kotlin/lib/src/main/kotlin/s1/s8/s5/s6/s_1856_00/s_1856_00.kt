/*
 * @lc app=leetcode.cn id=1856 lang=kotlin
 *
 * [1856] 子数组最小乘积的最大值
 */

package s1.s8.s5.s6.s_1856_00

// @lc code=start
const val MOD = 1_000_000_007L

class Solution {
    fun maxSumMinProduct(nums: IntArray): Int {
        val n = nums.size
        var left = IntArray(n)
        var right = IntArray(n)
        var stack = mutableListOf<Int>()
        var prefixSum = LongArray(n + 1)

        nums.forEachIndexed { i, num ->
            while (stack.isNotEmpty() && nums[stack.last()] >= num) {
                stack.removeLast()
            }
            left[i] = stack.lastOrNull() ?: -1
            stack.add(i)
        }

        stack.clear()
        nums.reversed().forEachIndexed { i, num ->
            val j = n - i - 1
            while (stack.isNotEmpty() && nums[stack.last()] >= num) {
                stack.removeLast()
            }
            right[j] = stack.lastOrNull() ?: n
            stack.add(j)
        }

        prefixSum[0] = 0
        nums.forEachIndexed { i, num ->
            prefixSum[i + 1] = prefixSum[i] + num
        }

        val result = nums.foldIndexed(0L) { i, result, num ->
            val product = (prefixSum[right[i]] - prefixSum[left[i] + 1]) * num
            maxOf(result, product)
        }
        return result.toInt()
    }
}
// @lc code=end
