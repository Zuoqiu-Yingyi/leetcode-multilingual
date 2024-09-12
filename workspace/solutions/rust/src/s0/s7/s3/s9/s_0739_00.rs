/*
 * @lc app=leetcode.cn id=739 lang=rust
 *
 * [739] 每日温度
 */

pub struct Solution {}

// @lc code=start
impl Solution {
    pub fn daily_temperatures(temperatures: Vec<i32>) -> Vec<i32> {
        /* 栈中的数单调递减 */
        let mut stack = vec![];
        /* 结果使用 0 初始化 */
        let mut result = vec![0; temperatures.len()];
        temperatures //
            .iter()
            .enumerate()
            .for_each(|(i, &t)| {
                /*
                当出现递增的数时
                    栈中的数出栈
                    直到栈顶元素大于当前数
                此时出栈的数一定比当前数小
                    之前出栈的数一定比此时出栈的数小
                    此时出栈的数 -> 当前数 是符合条件的映射
                 */
                while !stack.is_empty() && temperatures[*stack.last().unwrap()] < t {
                    let j = stack.pop().unwrap() as usize;
                    /*
                    对于第 j 天
                        第 i 天是第一个比第 j 天温度高的日子
                        因此第 (i - j) 天后温度会升高
                     */
                    result[j] = (i - j) as i32;
                }
                stack.push(i);
            });
        result
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::daily_temperatures;
