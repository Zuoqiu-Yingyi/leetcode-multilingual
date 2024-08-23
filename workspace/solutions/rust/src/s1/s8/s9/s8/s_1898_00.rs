/*
 * @lc app=leetcode.cn id=1898 lang=rust
 *
 * [1898] 可移除字符的最大数目
 */

pub struct Solution {}

// @lc code=start
use std::collections::HashSet;

impl Solution {
    pub fn maximum_removals(
        s: String,
        p: String,
        removable: Vec<i32>,
    ) -> i32 {
        /*
        字符串 s 移除 removable 中前 k 个指定位置的字符后
            若 p 仍为 s 的子序列
            则目标 k >= 当前 k
            具有单调性
                可以使用二分查找
         */
        let is_sub = |k: usize| -> bool {
            /* s 中会被移除的字符的下标 */
            let removed = removable[..k] //
                .iter()
                .collect::<HashSet<_>>();

            /* 判断 p 是否为移除一部分字符的 s 的子序列 */
            let mut sub_iter = p.chars();
            let mut sub_char = sub_iter.next().unwrap();
            for (str_index, str_char) in s.chars().enumerate() {
                /* 当前 p 的字符是 s 的有效字符, 检查下一个字符 */
                if str_char == sub_char && !removed.contains(&(str_index as i32)) {
                    if let Some(char) = sub_iter.next() {
                        /* p 序列还未检查完, 检查下一个字符 */
                        sub_char = char;
                    } else {
                        /* p 所有字符已经检查完, p 是子序列 */
                        return true;
                    }
                }
            }
            return false;
        };

        let mut left = 0;
        let mut right = removable.len();
        while left < right {
            let middle = (left + right) / 2;
            /* 判断是否为子序列 */
            if is_sub(middle + 1) {
                /* 是子序列, 搜索更大的 k */
                left = middle + 1;
            } else {
                /* 不是子序列, 搜索更小的 k */
                right = middle;
            }
        }
        left as i32
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::maximum_removals;
