/*
 * @lc app=leetcode.cn id=303 lang=golang
 *
 * [303] 区域和检索 - 数组不可变
 */

package s_0303_00

// @lc code=start
type NumArray struct {
	PrefixSums []int
}

func Constructor(nums []int) NumArray {
	prefixSums := make([]int, len(nums)+1)
	for i, v := range nums {
		prefixSums[i+1] = prefixSums[i] + v
	}
	return NumArray{
		PrefixSums: prefixSums,
	}
}

func (this *NumArray) SumRange(left int, right int) int {
	return this.PrefixSums[right+1] - this.PrefixSums[left]
}

/**
 * Your NumArray object will be instantiated and called as such:
 * obj := Constructor(nums);
 * param_1 := obj.SumRange(left,right);
 */
// @lc code=end

func (this NumArray) Func(left int, right int) int {
	return this.SumRange(left, right)
}

var Solution = Constructor
