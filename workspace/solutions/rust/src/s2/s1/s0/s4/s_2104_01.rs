/*
 * @lc app=leetcode.cn id=2104 lang=rust
 *
 * [2104] 子数组范围和
 */

pub struct Solution {}

// @lc code=start
use std::collections::VecDeque;

impl Solution {
    /// 单调栈
    pub fn sub_array_ranges(mut nums: Vec<i32>) -> i64 {
        let max = Self::f(&nums);
        nums.iter_mut().for_each(|x| *x = -(*x));
        let min = Self::f(&nums);
        max + min
    }

    /// 计算数组 nums 中每个元素作为最大值时的子数组范围和
    /// 要计算 nums 中每个元素作为最小值时的子数组范围和, 可以将 nums 正负号翻转后再次计算
    fn f(nums: &Vec<i32>) -> i64 {
        let n = nums.len();
        /* 单调递减栈 */
        let mut stack: VecDeque<usize> = VecDeque::new();
        /* left_index[i] 表示数组元素 nums[i] 左侧第一个 > 该元素的坐标 */
        let mut left_index = vec![-1; n];
        /* right_index[i] 表示数组元素 nums[i] 右侧第一个 >= 该元素的坐标 */
        let mut right_index = vec![n as i32; n];

        for (i, num) in nums.iter().enumerate() {
            /* 找到单调递减栈 stack 中第一个 nums[top] > num 的元素 */
            while !stack.is_empty() && nums[*stack.back().unwrap()] <= *num {
                stack.pop_back();
            }

            /*
            栈顶元素存在时
                nums[top] > num
                num 左侧第一个 > num 的坐标为 top
            栈顶元素不存在时
                num 左侧所有元素都 <= num
                左侧不存在挑战 num 最大值地位的元素
                left_index[i] 保持为默认值 -1
             */
            if let Some(&top) = stack.back() {
                left_index[i] = top as i32;
            }

            /*
            栈顶元素存在时
                nums[top] > num
                栈保持递减
            栈顶元素不存在时
                num 为栈中第一个元素
                栈保持递减
             */
            stack.push_back(i);
        }

        stack.clear();
        for (i, num) in nums.iter().rev().enumerate() {
            let j = n - i - 1;

            /* 找到单调递增栈 stack 中第一个 nums[top] >= num 的元素 */
            while !stack.is_empty() && nums[*stack.back().unwrap()] < *num {
                stack.pop_back();
            }

            /*
            栈顶元素存在时
                nums[top] >= num
                num 右侧第一个 >= num 的坐标为 top
            栈顶元素不存在时
                num 右侧所有元素都 < num
                右侧不存在能挑战 num 最大值地位的元素
                right_index[j] 保持为默认值 n
             */
            if let Some(&top) = stack.back() {
                right_index[j] = top as i32;
            }

            /*
            栈顶元素存在时
                nums[top] >= num
                栈保持递减
            栈顶元素不存在时
                num 为栈中第一个元素
                栈保持递减
             */
            stack.push_back(j);
        }

        let mut result = 0_i64;
        for (i, num) in nums.iter().enumerate() {
            let i_i32 = i as i32;

            /* 包含元素 num 的 (连续) 子数组左边界的组合数量 */
            let left = i_i32 - left_index[i];

            /* 包含元素 num 的 (连续) 子数组右边界的组合数量 */
            let right = right_index[i] - i_i32;

            /* 以元素 num 为最大值的 (连续) 子数组个数 */
            let count = (left * right) as i64;

            /*
            元素 num 为子数组最大值时
                num 每提高 1, 为最终结果贡献 count * num
            元素 num 为子数组最小值时
                num 每降低 1, 为最终结果贡献 count * num
             */
            result += count * (*num as i64);
        }
        return result;
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::sub_array_ranges;
