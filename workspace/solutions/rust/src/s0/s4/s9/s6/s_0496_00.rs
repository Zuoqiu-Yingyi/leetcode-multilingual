/*
 * @lc app=leetcode.cn id=496 lang=rust
 *
 * [496] 下一个更大元素 I
 */

pub struct Solution {}

// @lc code=start
use std::collections::HashMap;

impl Solution {
    pub fn next_greater_element(
        nums1: Vec<i32>,
        nums2: Vec<i32>,
    ) -> Vec<i32> {
        /* 数字 a -> nums3 中位置在 a 右侧且比 a 大的数 */
        let mut map: HashMap<i32, i32> = HashMap::new();
        /* 栈中的数单调递减 */
        let mut stack = vec![i32::MAX];
        for num in nums2 {
            /*
            当出现递增的数时
                栈中的数出栈
                直到栈顶元素大于当前数
            此时出栈的数一定比当前数小
                之前出栈的数一定比此时出栈的数小
                此时出栈的数 -> 当前数 是符合条件的映射
             */
            while stack.last().unwrap() < &num {
                map.insert(stack.pop().unwrap(), num);
            }
            /*
            栈顶的数比当前数大
                当前数入栈
             */
            stack.push(num);
        }
        nums1 //
            .iter()
            .map(|num| *map.get(num).unwrap_or(&-1))
            .collect()
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::next_greater_element;
