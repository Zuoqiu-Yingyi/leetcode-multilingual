/*
 * @lc app=leetcode.cn id=907 lang=rust
 *
 * [907] 子数组的最小值之和
 */

pub struct Solution {}

// @lc code=start
use std::collections::VecDeque;

const MOD: i64 = 1_000_000_007;

impl Solution {
    pub fn sum_subarray_mins(arr: Vec<i32>) -> i32 {
        let n = arr.len();
        /* 单调栈 */
        let mut stack: VecDeque<usize> = VecDeque::new();
        /*
        left_index[i] 表示数组元素 arr[i] 左侧第一个 < 该元素的坐标
        right_index[i] 表示数组元素 arr[i] 右侧第一个 <= 该元素的坐标
            若此处取 <, 则元素 arr[right_index[i]] 的区间与 arr[i] 的区间一致
                会重复计算相同的区间
        区间 (left_index[i], right_index[i]) 中的元素都 >= arr[i]
        若数组元素 arr[i] 左侧的元素都 >= 该元素, 则 left_index[i] 设置为 -1
        若数组元素 arr[i] 右侧的元素都 > 该元素, 则 right_index[i] 设置为 n
         */
        let mut left_index = vec![-1; n];
        let mut right_index = vec![n as i32; n];

        for (i, item) in arr.iter().enumerate() {
            /* stack 此时为单调递增栈 */
            while !stack.is_empty() && arr[*stack.back().unwrap()] >= *item {
                stack.pop_back();
            }
            /* 此时栈顶元素对应的值 < item */
            if let Some(&top) = stack.back() {
                left_index[i] = top as i32;
            }
            stack.push_back(i);
        }

        stack.clear();
        for (i, item) in arr.iter().rev().enumerate() {
            let j = n - i - 1;
            /* stack 此时为单调递增栈 */
            while !stack.is_empty() && arr[*stack.back().unwrap()] > *item {
                stack.pop_back();
            }
            /* 此时栈顶元素对应的值 <= item */
            if let Some(&top) = stack.back() {
                right_index[j] = top as i32;
            }
            stack.push_back(j);
        }

        let mut result = 0;
        for i in 0..n {
            let i_i32 = i as i32;
            /* 元素 arr[i] 左侧符合条件的元素个数 (包含 arr[i] 自身) */
            let left = i_i32 - left_index[i];
            /* 元素 arr[i] 右侧符合条件的元素个数 (包含 arr[i] 自身) */
            let right = right_index[i] - i_i32;
            /* 以元素 arr[i] 为最小值的 (连续) 子数组个数 */
            let count = (left * right) as i64;
            result += (count * (arr[i] as i64)) % MOD;
            result %= MOD;
        }
        return result as i32;
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::sum_subarray_mins;
