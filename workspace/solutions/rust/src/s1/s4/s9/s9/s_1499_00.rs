/*
 * @lc app=leetcode.cn id=1499 lang=rust
 *
 * [1499] 满足不等式的最大值
 */

pub struct Solution {}

// @lc code=start
use std::collections::VecDeque;

impl Solution {
    pub fn find_max_value_of_equation(
        points: Vec<Vec<i32>>,
        k: i32,
    ) -> i32 {
        let n = points.len();
        /*
        由于 i < j 时有 x_i < x_j
        所以 y_i + y_j + |x_i - x_j| = y_i + y_j + x_j - x_i = (y_i - x_i) + (x_j + y_j)
        窗口宽度为 k
         */
        let mut result = std::i32::MIN;
        /*
        deque 是一个单调递减队列
            当 i0 < i1 时
            eval(i0, j) < eval(i1, j)
        deque.front 是满足条件 |xi - xj| <= k 的最大值对应的 i
         */
        let mut queue: VecDeque<usize> = VecDeque::new();

        /*
        求值
        y_i + y_j + |x_i - x_j|
            = y_i + y_j + x_j - x_i
            = (y_i - x_i) + (x_j + y_j)
         */
        let eval = |i: &usize, j: &usize| -> i32 { (points[*i][1] - points[*i][0]) + (points[*j][0] + points[*j][1]) };

        /*
        比较两值 eval(i0, j) 与 eval(i1, j)
        由于 j 是同一个值
            (y_i0 - x_i0) + (x_j + y_j)
            (y_i1 - x_i1) + (x_j + y_j)
        因此可以仅比较 (y_i0 - x_i0) 与 (y_i1 - x_i1)
        当 eval(i0, j) < eval(i1, j) 时
        compare(i0, i1) < 0
         */
        let compare = |i0: &usize, i1: &usize| -> i32 { (points[*i0][1] - points[*i0][0]) - (points[*i1][1] - points[*i1][0]) };

        /*
        区间是否在窗口内
        |x_i - x_j| = x_j - x_i <= k
         */
        let range_in_window = |i: &usize, j: &usize| -> bool { (points[*j][0] - points[*i][0]) <= k };

        for j in 0..n {
            /*
            滑动窗口右移
            移除左侧滑出窗口的元素
             */
            while !queue.is_empty() && !range_in_window(queue.front().unwrap(), &j) {
                queue.pop_front();
            }

            /*
            求最大值
            由于 queue 是单调递减队列
            所以 queue.front 是满足条件 eval(i, j) 的最大值对应的 i
             */
            if let Some(i) = queue.front() {
                result = result.max(eval(i, &j));
            }

            /*
            保持单调性
            移除所有不满足条件 eval(i0, j) <= eval(i1, j)
             */
            while !queue.is_empty() && compare(queue.back().unwrap(), &j) <= 0 {
                queue.pop_back();
            }

            queue.push_back(j);
        }

        result
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::find_max_value_of_equation;
