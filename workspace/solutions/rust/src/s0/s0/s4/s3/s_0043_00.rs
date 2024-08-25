/*
 * @lc app=leetcode.cn id=43 lang=rust
 *
 * [43] 字符串相乘
 */

pub struct Solution {}

// @lc code=start
impl Solution {
    pub fn multiply(
        num1: String,
        num2: String,
    ) -> String {
        /* 排除结果恒为 0 的情况 */
        if num1 == "0" || num2 == "0" {
            return String::from("0");
        }

        let (chars_1, chars_2) = (num1.as_bytes(), num2.as_bytes());
        let (chars_1_len, chars_2_len) = (num1.len(), num2.len());

        let mut result: Vec<u8> = vec![];

        /* 从低位到高位遍历 num1 */
        for i in 0..chars_1.len() {
            let multiplier_1 = chars_1[chars_1_len - i - 1] - b'0';
            let mut over = 0_u8;
            let mut index = 0_usize;

            /*
            求和 = 两个数的当前位的乘积 + 之前的进位 + 之前求和结果当前位的值

            @param: index - 当前处理的数位
            @param: over - 上次计算进位数
            @param: product - 两个数的当前位的乘积

            @returns: 本次计算进位数
            */
            let mut sum = |index: &usize, over: &u8, product: u8| {
                /* 填充结果的高位为 0 */
                while *index >= result.len() {
                    result.push(0);
                }

                let value = product + over + result[*index];
                result[*index] = value % 10;
                value / 10
            };

            /* 从低位到高位遍历 num2 */
            for j in 0..chars_2.len() {
                let multiplier_2 = chars_2[chars_2_len - j - 1] - b'0';
                index = i + j;
                over = sum(&index, &over, multiplier_1 * multiplier_2);
            }

            /* 处理进位 */
            while over > 0 {
                index += 1;
                over = sum(&index, &over, 0);
            }
        }

        result //
            .iter()
            .rev()
            .map(|&x| (x + b'0') as char)
            .collect()
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::multiply;
