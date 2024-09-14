/*
 * @lc app=leetcode.cn id=962 lang=rust
 *
 * [962] 最大宽度坡
 */

pub struct Solution {}

// @lc code=start
impl Solution {
    pub fn max_width_ramp(nums: Vec<i32>) -> i32 {
        let n = nums.len();
        let mut stack: Vec<usize> = Vec::new();

        for (i, &num) in nums.iter().enumerate() {
            if stack.is_empty() || nums[*stack.last().unwrap()] > num {
                stack.push(i);
            }
        }

        let mut result = 0;
        for (i, &num) in nums.iter().rev().enumerate() {
            let j = n - i - 1;
            while !stack.is_empty() && nums[*stack.last().unwrap()] <= num {
                result = result.max(j - stack.pop().unwrap());
            }
            if stack.is_empty() {
                break;
            }
        }
        result as i32
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::max_width_ramp;
