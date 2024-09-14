/*
 * @lc app=leetcode.cn id=962 lang=rust
 *
 * [962] 最大宽度坡
 */

pub struct Solution {}

// @lc code=start
use std::collections::VecDeque;

impl Solution {
    pub fn max_width_ramp(nums: Vec<i32>) -> i32 {
        let n = nums.len();
        /*
        存储所有可能的 i 值
            i 单调递增
        单调递减栈
            nums[i] 单调递减
            若 存在 i_1 < i_2 且 nums[i_1] <= nums[i_2]
                则坡 (i_1, j) 的宽度 (j - i_1) 一定比坡 (i_2, j) 的宽度 (j - i_2) 大
                因此 i_2 不可能是最大宽度坡的左端点
                因此 nums[i] 单调递减
         */
        let mut stack: VecDeque<usize> = VecDeque::new();

        /* 将所有可能的 i 值保存在单调递减栈 stack 中 */
        for (i, &num) in nums.iter().enumerate() {
            if stack.is_empty() || nums[*stack.back().unwrap()] > num {
                stack.push_back(i);
            }
        }

        let mut result = 0;
        for (i, &num) in nums.iter().rev().enumerate() {
            let j = n - i - 1;
            /*
            指定 j 后, 判断栈顶的 i 是否满足条件 nums[i] <= nums[j]
                若满足
                    计算坡的宽度
                    弹出栈顶元素
                        j 减小后针对同一个 i, (j - i) 减小, 不可能找到更大的宽度坡
             */
            while !stack.is_empty() && nums[*stack.back().unwrap()] <= num {
                result = result.max(j - stack.pop_back().unwrap());
            }
        }
        result as i32
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::max_width_ramp;
