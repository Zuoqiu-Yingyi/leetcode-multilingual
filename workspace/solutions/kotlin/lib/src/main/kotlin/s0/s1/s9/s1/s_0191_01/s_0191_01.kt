/*
 * @lc app=leetcode.cn id=191 lang=kotlin
 *
 * [191] 位1的个数
 */

package s0.s1.s9.s1.s_0191_01

// @lc code=start
class Solution {
    fun hammingWeight(n: Int): Int {
        var count = 0
        var num = n
        while (num != 0) {
            num -= num and (-num)
            count++
        }
        return count
    }
}
// @lc code=end
