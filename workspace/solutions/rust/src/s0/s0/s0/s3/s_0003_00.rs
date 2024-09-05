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
        let mut char_set = std::collections::HashSet::<u8>::new();

        /*
        左侧的指针
        指示当前窗口的左边界
         */
        let mut left = 0;
        chars
            .iter()
            .map(|&c| {
                /*
                若当前字符已经在窗口中
                    说明 left 到当前字符之间的字符串中存在重复的字符
                移动左指针，直到窗口中不再有当前字符 (当前字符不重复)
                 */
                while char_set.contains(&c) {
                    char_set.remove(&chars[left]);
                    left += 1;
                }

                /*
                此时集合 char_set 中一定不存在当前字符
                left 到当前字符的字符串中不存在重复字符
                 */
                char_set.insert(c);
                /*
                由于 left 到当前字符的字符串中不存在重复字符
                    left 到当前字符的字符串长度等于 char_set 集合中的字符数量
                 */
                char_set.len()
            })
            .max() // 返回最大值, 即最长的子字符串长度
            .unwrap_or(0) as i32 // 若字符串为空, 则返回 0
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::length_of_longest_substring;
