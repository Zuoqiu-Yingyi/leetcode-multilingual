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
            n - (n & -n) 会将 n 的最低位的 1 置为 0
                n & -n 会将 n 的最低位的 1 保留, 其他位置为 0
            |      n |     -n | n & -n | n - (n & -n) |
            | -----: | -----: | -----: | -----------: |
            | 0..001 | 1..111 | 0..001 |       0..000 |
            | 0..010 | 1..110 | 0..010 |       0..000 |
            | 0..011 | 1..101 | 0..001 |       0..010 |
            | 0..100 | 1..100 | 0..100 |       0..000 |
            | 0..101 | 1..011 | 0..001 |       0..100 |
            | 0..110 | 1..010 | 0..010 |       0..100 |
            | 0..111 | 1..001 | 0..001 |       0..110 |
             */
            n -= n & -n;
            count += 1;
        }

        count
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::hamming_weight;
