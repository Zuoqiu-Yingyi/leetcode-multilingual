/*
 * @lc app=leetcode.cn id=713 lang=rust
 *
 * [713] 乘积小于 K 的子数组
 */

pub struct Solution {}

// @lc code=start
impl Solution {
    pub fn num_subarray_product_less_than_k(
        nums: Vec<i32>,
        k: i32,
    ) -> i32 {
        let (count, _, _) = nums //
            .iter()
            .enumerate()
            .fold(
                (0_usize, 0_usize, 1_i32), //
                |(mut count, mut left, mut product), (right, &num)| {
                    /*
                    窗口右边界右移
                    计算窗口中所有元素的乘积
                     */
                    product *= num;

                    /*
                    当乘积大于等于 k 时，窗口左边界右移
                    窗口左边界 == 窗口右边界
                        left == right
                        窗口中只有一个元素
                    窗口左边界 > 窗口右边界
                        left -1 == right
                        窗口中没有元素
                     */
                    while product >= k && left <= right {
                        product /= nums[left];
                        left += 1;
                    }
                    /*
                    left < right
                        窗口中有 (right - left + 1) 个元素
                        满足条件的连续子数组个数为 (right - left + 1)
                            [right]
                            [right-1, right]
                            [right-2, right-1, right]
                            ...
                            [left+1, ..., right-1, right]
                            [left, left+1, ..., right-1, right]
                    left == right
                        窗口中有 (1 = right - left + 1) 个元素
                        满足条件的连续子数组个数为 1 (即该元素组成的数组)
                    left > right
                        窗口中有 (0 = right - left + 1) 个元素
                        满足条件的连续子数组个数为 0
                    !注意: right - left 可能为 -1, 需要避免溢出
                     */
                    count += 1 + right - left;
                    (count, left, product)
                },
            );

        count as i32
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::num_subarray_product_less_than_k;
