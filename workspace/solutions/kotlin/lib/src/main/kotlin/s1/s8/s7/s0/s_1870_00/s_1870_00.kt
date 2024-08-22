/*
 * @lc app=leetcode.cn id=1870 lang=kotlin
 *
 * [1870] 准时到达的列车最小时速
 */

package s1.s8.s7.s0.s_1870_00

// @lc code=start
class Solution {
    fun minSpeedOnTime(dist: IntArray, hour: Double): Int {
        val n = dist.size
        if (n > Math.ceil(hour)) {
            return -1
        }
        val M = 1e7.toInt()
        var left = 1
        var right = M + 1
        while (left < right) {
            val middle = (left + right) / 2
            var time = 0.0
            dist.forEachIndexed { i, item ->
                val t = item.toDouble() / middle
                time += if (i == n - 1) t else Math.ceil(t)
            }
            if (time > hour) {
                left = middle + 1
            } else {
                right = middle
            }
        }
        return if (left > M) -1 else left
    }
}
// @lc code=end
