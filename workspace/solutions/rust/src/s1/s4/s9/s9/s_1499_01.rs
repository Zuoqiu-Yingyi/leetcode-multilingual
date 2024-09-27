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
        let mut result = std::i32::MIN;
        let mut queue: VecDeque<usize> = VecDeque::new();

        for (j, point) in points.iter().enumerate() {
            let x = point[0];
            let y = point[1];

            while !queue.is_empty() && (x - points[*queue.front().unwrap()][0]) > k {
                queue.pop_front();
            }

            if let Some(i) = queue.front() {
                result = result.max((points[*i][1] - points[*i][0]) + (x + y));
            }

            while let Some(i) = queue.back() {
                if (points[*i][1] - points[*i][0]) <= (y - x) {
                    queue.pop_back();
                } else {
                    break;
                }
            }

            queue.push_back(j);
        }

        result
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::find_max_value_of_equation;
