/*
 * @lc app=leetcode.cn id=3 lang=kotlin
 *
 * [3] 无重复字符的最长子串
 */

package s0.s0.s0.s3.s_0003_00

// @lc code=start
class Solution {
    fun lengthOfLongestSubstring(s: String): Int {
        var char_set = BooleanArray(128)
        var left = 0
        var ans = 0
        s.forEachIndexed { right, c ->
            while (char_set[c.code]) {
                char_set[s[left].code] = false
                left++
            }
            char_set[c.code] = true
            ans = Math.max(ans, right - left + 1)
        }
        return ans
    }
}
// @lc code=end
