/*
 * @lc app=leetcode.cn id=1 lang=golang
 *
 * [1] 两数之和
 */

package s_0001_00

// @lc code=start
func twoSum(nums []int, target int) []int {
	_map := map[int]int{}
	for i := 0; ; i++ {
		num := nums[i]
		diff := target - num
		if j, ok := _map[diff]; ok {
			return []int{j, i}
		}
		_map[num] = i
	}
}

// @lc code=end

var Solution = twoSum
