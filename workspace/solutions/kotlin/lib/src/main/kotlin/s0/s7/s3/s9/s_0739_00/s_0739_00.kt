/*
 * @lc app=leetcode.cn id=739 lang=kotlin
 *
 * [739] 每日温度
 */

package s0.s7.s3.s9.s_0739_00

// @lc code=start
class Solution {
    fun dailyTemperatures(temperatures: IntArray): IntArray {
        var stack = mutableListOf<Int>()
        var result = IntArray(temperatures.size) { 0 }
        temperatures.forEachIndexed { i, t ->
            while (stack.isNotEmpty() && temperatures[stack.last()] < t) {
                val j = stack.removeLast()
                result[j] = i - j
            }
            stack.add(i)
        }
        return result
    }
}
// @lc code=end
