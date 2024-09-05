/*
 * @lc app=leetcode.cn id=191 lang=rust
 *
 * [191] 位1的个数
 */

pub struct Solution {}

// @lc code=start
impl Solution {
    pub fn hamming_weight(n: i32) -> i32 {
        n.count_ones() as i32
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::hamming_weight;
