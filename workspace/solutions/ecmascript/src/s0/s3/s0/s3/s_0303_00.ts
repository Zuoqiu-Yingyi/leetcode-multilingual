/*
 * @lc app=leetcode.cn id=303 lang=typescript
 *
 * [303] 区域和检索 - 数组不可变
 */

// @lc code=start
class NumArray {
    private prefix_sums: number[];

    constructor(nums: number[]) {
        this.prefix_sums = nums.slice();
        this.prefix_sums.reduce((sum, value, index, sums) => {
            sum += value;
            sums[index] = sum;
            return sum;
        }, 0);
    }

    sumRange(left: number, right: number): number {
        return (this.prefix_sums[right] ?? 0) - (this.prefix_sums[left - 1] ?? 0);
    }
}

/**
 * Your NumArray object will be instantiated and called as such:
 * var obj = new NumArray(nums)
 * var param_1 = obj.sumRange(left,right)
 */
// @lc code=end

export default class extends NumArray {
    public func = this.sumRange;
    constructor(nums: number[]) {
        super(nums);
    }
};
