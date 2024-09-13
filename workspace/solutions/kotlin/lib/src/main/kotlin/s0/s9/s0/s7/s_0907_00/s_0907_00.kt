/*
 * @lc app=leetcode.cn id=907 lang=kotlin
 *
 * [907] 子数组的最小值之和
 */

package s0.s9.s0.s7.s_0907_00

// @lc code=start
class Solution {
    companion object {
        val MOD = 1_000_000_007L
    }

    fun sumSubarrayMins(arr: IntArray): Int {
        val n = arr.size
        var stack = mutableListOf<Int>()
        var left = IntArray(n)
        var right = IntArray(n)

        arr.forEachIndexed { i, item ->
            while (stack.isNotEmpty() && arr[stack.last()] >= item) {
                stack.removeLast()
            }
            left[i] = stack.lastOrNull() ?: -1
            stack.add(i)
        }

        stack.clear()
        arr.reversed().forEachIndexed { i, item ->
            val j = n - i - 1
            while (stack.isNotEmpty() && arr[stack.last()] > item) {
                stack.removeLast()
            }
            right[j] = stack.lastOrNull() ?: n
            stack.add(j)
        }

        var result = 0L
        for (i in arr.indices) {
            result = (result + (i - left[i]).toLong() * (right[i] - i) * arr[i]) % MOD
        }
        return result.toInt()
    }
}
// @lc code=end
