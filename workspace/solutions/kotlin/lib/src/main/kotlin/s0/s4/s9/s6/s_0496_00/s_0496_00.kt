/*
 * @lc app=leetcode.cn id=496 lang=kotlin
 *
 * [496] 下一个更大元素 I
 */

package s0.s4.s9.s6.s_0496_00

// @lc code=start
class Solution {
    fun nextGreaterElement(nums1: IntArray, nums2: IntArray): IntArray {
        var map = mutableMapOf<Int, Int>()
        var stack = mutableListOf<Int>(Int.MAX_VALUE)
        nums2.forEach {
            while (stack.last() < it) {
                map[stack.removeLast()] = it
            }
            stack.add(it)
        }
        return nums1.map { map[it] ?: -1 }.toIntArray()
    }
}
// @lc code=end
