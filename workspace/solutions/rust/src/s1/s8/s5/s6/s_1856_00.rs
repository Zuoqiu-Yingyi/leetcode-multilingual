/*
 * @lc app=leetcode.cn id=1856 lang=rust
 *
 * [1856] 子数组最小乘积的最大值
 */

pub struct Solution {}

// @lc code=start
const MOD: i64 = 1_000_000_007;

impl Solution {
    pub fn max_sum_min_product(nums: Vec<i32>) -> i32 {
        /* 使用单调栈计算数组每个元素的作为最小值的最大子数组 */
        let n = nums.len();
        /* left_index[i] 表示数组元素 nums[i] 左侧第一个 < nums[i] 的坐标 */
        let mut left_index = vec![-1; n];
        /* right_index[i] 表示数组元素 nums[i] 右侧第一个 <= nums[i] 的坐标 */
        let mut right_index = vec![n as i32; n];
        /* 单调递增栈 */
        let mut stack = std::collections::VecDeque::new();
        /* nums 的前缀和 */
        let mut prefix_sums = vec![0_i64; n + 1];

        /* 计算子数组左边界 */
        for (i, &num) in nums.iter().enumerate() {
            while !stack.is_empty() && nums[*stack.back().unwrap()] >= num {
                stack.pop_back();
            }
            if let Some(&top) = stack.back() {
                left_index[i] = top as i32;
            }
            stack.push_back(i);
        }

        /* 计算子数组右边界 */
        stack.clear();
        for (i, &num) in nums.iter().rev().enumerate() {
            let j = n - i - 1;
            while !stack.is_empty() && nums[*stack.back().unwrap()] > num {
                stack.pop_back();
            }
            if let Some(&top) = stack.back() {
                right_index[j] = top as i32;
            }
            stack.push_back(j);
        }

        /* 计算数组前缀和 */
        for (i, &num) in nums.iter().enumerate() {
            prefix_sums[i + 1] = prefix_sums[i] + (num as i64);
        }

        /* 计算每个元素对应的子数组最小乘积的最大值 */
        let result = nums.iter().enumerate().fold(0_i64, |result, (i, &num)| -> i64 {
            let left = left_index[i];
            let right = right_index[i];
            let sum = prefix_sums[right as usize] - prefix_sums[(left + 1) as usize];
            return result.max(sum * (num as i64));
        });

        (result % MOD) as i32
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::max_sum_min_product;
