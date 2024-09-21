/*
 * @lc app=leetcode.cn id=303 lang=java
 *
 * [303] 区域和检索 - 数组不可变
 */

package s0.s3.s0.s3.s_0303_00;

// @lc code=start
class NumArray {
    private int[] prefix_sums;

    public NumArray(
        int[] nums
    ) {
        final int nums_length = nums.length;
        this.prefix_sums = new int[nums_length + 1];
        for (int i = 0; i < nums_length; i++) {
            this.prefix_sums[i + 1] = this.prefix_sums[i] + nums[i];
        }
    }

    public int sumRange(
        int left, int right
    ) {
        return this.prefix_sums[right + 1] - this.prefix_sums[left];
    }
}

/**
 * Your NumArray object will be instantiated and called as such:
 * NumArray obj = new NumArray(nums);
 * int param_1 = obj.sumRange(left,right);
 */
// @lc code=end

public class s_0303_00 extends NumArray {
    public s_0303_00(
        int[] nums
    ) {
        super(nums);
    }
}
