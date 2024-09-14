/*
 * @lc app=leetcode.cn id=2104 lang=rust
 *
 * [2104] 子数组范围和
 */

pub struct Solution {}

// @lc code=start
impl Solution {
    /// 暴力枚举
    pub fn sub_array_ranges(nums: Vec<i32>) -> i64 {
        let n = nums.len();
        let mut result = 0_i64;

        for i in 0..(n - 1) {
            let mut min = nums[i];
            let mut max = nums[i];

            for j in (i + 1)..n {
                min = min.min(nums[j]);
                max = max.max(nums[j]);
                result += (max - min) as i64;
            }
        }

        result
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::sub_array_ranges;
