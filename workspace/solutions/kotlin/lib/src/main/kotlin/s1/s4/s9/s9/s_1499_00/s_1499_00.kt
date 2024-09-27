/*
 * @lc app=leetcode.cn id=1499 lang=kotlin
 *
 * [1499] 满足不等式的最大值
 */

package s1.s4.s9.s9.s_1499_00

// @lc code=start
class Solution {
    fun findMaxValueOfEquation(points: Array<IntArray>, k: Int): Int {
        var result = Int.MIN_VALUE
        var queue = mutableListOf<Int>()

        points.forEachIndexed { j, point ->
            val x = point[0]
            val y = point[1]

            while (queue.isNotEmpty() && (x - points[queue[0]][0]) > k) {
                queue.removeFirst()
            }

            if (queue.isNotEmpty()) {
                val i = queue.first()
                result = maxOf(result, points[i][1] - points[i][0] + x + y)
            }

            while (queue.isNotEmpty()) {
                val i = queue.last()
                if (points[i][1] - points[i][0] <= y - x) {
                    queue.removeLast()
                } else {
                    break
                }
            }

            queue.add(j)
        }

        return result
    }
}
// @lc code=end
