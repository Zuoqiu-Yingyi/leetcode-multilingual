/*
 * @lc app=leetcode.cn id=1898 lang=kotlin
 *
 * [1898] 可移除字符的最大数目
 */

package s1.s8.s9.s8.s_1898_00

// @lc code=start
class Solution {
    fun maximumRemovals(s: String, p: String, removable: IntArray): Int {
        val strLen = s.length
        val subLen = p.length

        fun isSub(k: Int): Boolean {
            val removed = removable.sliceArray(0..<k).toHashSet()

            var subIndex = 0
            for (strIndex in 0..<strLen) {
                if (s[strIndex] == p[subIndex] && !removed.contains(strIndex)) {
                    ++subIndex
                    if (subIndex >= subLen) {
                        break
                    }
                }
            }

            return subIndex == subLen
        }

        var left = 0
        var right = removable.size

        while (left < right) {
            val middle = (left + right) / 2
            if (isSub(middle + 1)) {
                left = middle + 1
            } else {
                right = middle
            }
        }
        return left
    }
}
// @lc code=end
