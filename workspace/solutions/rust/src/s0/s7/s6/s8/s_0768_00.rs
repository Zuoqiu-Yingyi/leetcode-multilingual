/*
 * @lc app=leetcode.cn id=768 lang=rust
 *
 * [768] 最多能完成排序的块 II
 */

pub struct Solution {}

// @lc code=start
impl Solution {
    pub fn max_chunks_to_sorted(arr: Vec<i32>) -> i32 {
        /*
        (非严格) 单调递增栈
        里面存放已遍历元素中各个分组的最大值
        每个分组的最大值 x 一定是分组内的最左侧值
            若分组最大值右侧存在 >= x
            则该值一定在另一个分组内
         */
        let mut stack: Vec<i32> = vec![];
        for num in arr.iter() {
            if stack.is_empty() || stack.last().unwrap() <= num {
                /*
                当前元素 >= 之前所有分组的最大值
                该元素可以自成一个分组
                 */
                stack.push(*num);
            } else {
                /*
                当前元素 num < 之前所有分组的最大值 max
                将当前元素 num 添加左侧相邻的分组
                    原分组的最大值 max 也是新分组的最大值
                 */
                let max = stack.pop().unwrap();

                /*
                当前元素 num < 左侧分组的最大值
                    说明若新分组不能满足要求
                        各分组排序后, 左侧分组最大值一定在新分组值 num 的左侧, 不满足题意
                    需要再合并一个分组
                不断进行该以上步骤直到新分组满足要求
                 */
                while !stack.is_empty() && num < stack.last().unwrap() {
                    stack.pop();
                }

                /* 原分组的最大值 max 也是新分组的最大值 */
                stack.push(max);
            }
        }
        stack.len() as i32
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::max_chunks_to_sorted;
