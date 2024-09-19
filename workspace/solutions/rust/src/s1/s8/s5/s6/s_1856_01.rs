/*
 * @lc app=leetcode.cn id=1856 lang=rust
 *
 * [1856] 子数组最小乘积的最大值
 */

use std::vec;

pub struct Solution {}

// @lc code=start
const MOD: i64 = 1_000_000_007;

impl Solution {
    pub fn max_sum_min_product(nums: Vec<i32>) -> i32 {
        let n = nums.len();
        let mut left = vec![-1; n];
        let mut right = vec![n as i32; n];
        let mut stack: Vec<usize> = vec![];
        let mut prefix_sums = vec![0_i64; n + 1];

        for (i, &num) in nums.iter().enumerate() {
            while !stack.is_empty() && nums[*stack.last().unwrap()] >= num {
                stack.pop();
            }
            if let Some(&top) = stack.last() {
                left[i] = top as i32;
            }
            stack.push(i);
        }

        stack.clear();
        for (i, &num) in nums.iter().rev().enumerate() {
            let j = n - i - 1;
            while !stack.is_empty() && nums[*stack.last().unwrap()] > num {
                stack.pop();
            }
            if let Some(&top) = stack.last() {
                right[j] = top as i32;
            }
            stack.push(j);
        }

        for (i, &num) in nums.iter().enumerate() {
            prefix_sums[i + 1] = prefix_sums[i] + (num as i64);
        }

        let result = nums //
            .iter()
            .enumerate()
            .fold(0_i64, |result, (i, &num)| -> i64 {
                let left = left[i];
                let right = right[i];
                let sum = prefix_sums[right as usize] - prefix_sums[(left + 1) as usize];
                return result.max(sum * (num as i64));
            });

        (result % MOD) as i32
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::max_sum_min_product;
