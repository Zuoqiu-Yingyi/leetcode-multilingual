/*
 * @lc app=leetcode.cn id=3 lang=rust
 *
 * [3] 无重复字符的最长子串
 */

pub struct Solution {}

// @lc code=start
impl Solution {
    pub fn length_of_longest_substring(s: String) -> i32 {
        let chars = s.as_bytes();

        /* 使用数组替代哈希集合 */
        let mut char_set = [false; 128];
        let mut left = 0;

        chars
            .iter()
            .enumerate()
            .map(|(right, c)| {
                let c = *c as usize;
                while char_set[c] {
                    char_set[chars[left] as usize] = false;
                    left += 1;
                }
                char_set[c] = true;
                right - left + 1
            })
            .max()
            .unwrap_or(0) as i32
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::length_of_longest_substring;
