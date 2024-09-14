/*
 * @lc app=leetcode.cn id=768 lang=kotlin
 *
 * [768] 最多能完成排序的块 II
 */

package s0.s7.s6.s8.s_0768_00

// @lc code=start
class Solution {
    fun maxChunksToSorted(arr: IntArray): Int {
        val stack = mutableListOf<Int>()
        for (num in arr) {
            if (stack.isEmpty() || stack.last() <= num) {
                stack.add(num)
            } else {
                val max = stack.removeLast()
                while (stack.isNotEmpty() && num < stack.last()) {
                    stack.removeLast()
                }
                stack.add(max)
            }
        }
        return stack.size
    }
}
// @lc code=end
