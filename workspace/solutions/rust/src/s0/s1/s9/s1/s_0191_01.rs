/*
 * @lc app=leetcode.cn id=191 lang=rust
 *
 * [191] 位1的个数
 */

pub struct Solution {}

// @lc code=start
impl Solution {
    pub fn hamming_weight(n: i32) -> i32 {
        let mut count = 0;
        let mut n = n;

        while n != 0 {
            /*
            n & (n - 1) 会将 n 的最低位的 1 置为 0
                n-1 会将 n 的最低位的 1 变为 0, 且将更低位的 0 变为 1
            |   n | n - 1 | n & (n - 1) |
            | --: | ----: | ----------: |
            |   1 |     0 |           0 |
            |  10 |    01 |           0 |
            |  11 |    10 |          10 |
            | 100 |   011 |           0 |
            | 101 |   100 |         100 |
            | 110 |   101 |         100 |
            | 111 |   110 |         110 |
             */
            n &= n - 1;
            count += 1;
        }

        count
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::hamming_weight;
