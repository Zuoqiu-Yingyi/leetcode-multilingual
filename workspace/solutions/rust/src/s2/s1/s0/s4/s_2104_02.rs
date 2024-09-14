/*
 * @lc app=leetcode.cn id=2104 lang=rust
 *
 * [2104] 子数组范围和
 */

pub struct Solution {}

// @lc code=start
impl Solution {
    pub fn sub_array_ranges(mut nums: Vec<i32>) -> i64 {
        let max = Self::f(&nums);
        nums.iter_mut().for_each(|x| *x = -(*x));
        let min = Self::f(&nums);
        max + min
    }

    fn f(nums: &Vec<i32>) -> i64 {
        let n = nums.len();
        let mut stack: Vec<usize> = vec![];
        let mut left_index = vec![-1; n];
        let mut right_index = vec![n as i32; n];

        for (i, num) in nums.iter().enumerate() {
            while !stack.is_empty() && nums[*stack.last().unwrap()] <= *num {
                stack.pop();
            }
            if let Some(&top) = stack.last() {
                left_index[i] = top as i32;
            }
            stack.push(i);
        }

        stack.clear();
        for (i, num) in nums.iter().rev().enumerate() {
            let j = n - i - 1;
            while !stack.is_empty() && nums[*stack.last().unwrap()] < *num {
                stack.pop();
            }
            if let Some(&top) = stack.last() {
                right_index[j] = top as i32;
            }
            stack.push(j);
        }

        nums.iter().enumerate().fold(0_i64, |acc, (i, num)| acc + (((i as i32 - left_index[i]) * (right_index[i] - i as i32)) as i64) * (*num as i64))
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::sub_array_ranges;
