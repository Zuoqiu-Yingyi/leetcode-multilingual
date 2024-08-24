/*
 * @lc app=leetcode.cn id=415 lang=rust
 *
 * [415] 字符串相加
 */

pub struct Solution {}

// @lc code=start
impl Solution {
    pub fn add_strings(
        num1: String,
        num2: String,
    ) -> String {
        let mut result: Vec<u8> = vec![];
        let chars_1 = num1.as_bytes();
        let chars_2 = num2.as_bytes();

        /*
        当前处理的数位
        字符串高位对应的是数字的低位
         */
        let (mut i, mut j) = (chars_1.len(), chars_2.len());

        /* 进位数 */
        let mut over = 0_u8;

        while i > 0 || j > 0 || over > 0 {
            let mut sum = over;
            if i > 0 {
                sum += chars_1[i - 1] - b'0';
                i -= 1;
            }
            if j > 0 {
                sum += chars_2[j - 1] - b'0';
                j -= 1;
            }
            over = sum / 10;
            result.push(sum % 10);
        }

        /*
        字符串低位对应的是数字的高位
        需要反转字符串
         */
        result //
            .iter()
            .rev()
            .map(|&x| (x + b'0') as char)
            .collect()
    }
}

// @lc code=end

pub const SOLUTION: super::TSolution = Solution::add_strings;
