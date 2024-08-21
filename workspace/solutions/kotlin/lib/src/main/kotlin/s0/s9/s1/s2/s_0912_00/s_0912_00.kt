/*
 * @lc app=leetcode.cn id=912 lang=kotlin
 *
 * [912] 排序数组
 */

package s0.s9.s1.s2.s_0912_00

// @lc code=start
class Solution {
    fun sortArray(nums: IntArray): IntArray {
        fun quickSort(left: Int, right: Int) {
            if (left >= right) {
                return
            }
            var i = left - 1
            var j = right + 1
            val pivot = nums[left]
            while (i < j) {
                while (nums[++i] < pivot) ;
                while (nums[--j] > pivot) ;
                if (i < j) {
                    val temp = nums[i]
                    nums[i] = nums[j]
                    nums[j] = temp
                }
            }
            quickSort(left, j)
            quickSort(j + 1, right)
        }
        quickSort(0, nums.size - 1)
        return nums
    }
}
// @lc code=end
