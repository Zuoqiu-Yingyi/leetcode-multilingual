/*
 * @lc app=leetcode.cn id=1 lang=kotlin
 *
 * [1] 两数之和
 */

package s0.s0.s0.s1.s_0001_00

// @lc code=start
class Solution {
    fun twoSum(nums: IntArray, target: Int): IntArray {
        val map = mutableMapOf<Int, Int>()
        nums.forEachIndexed { i, num ->
            val diff = target - num
            val j = map.get(diff)
            if (j != null) {
                return intArrayOf(j, i)
            }
            map[num] = i
        }
        return intArrayOf()
    }
}
// @lc code=end
