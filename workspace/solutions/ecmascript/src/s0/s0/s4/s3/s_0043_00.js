/*
 * @lc app=leetcode.cn id=43 lang=javascript
 *
 * [43] 字符串相乘
 */

// @lc code=start
/**
 * @param {string} num1
 * @param {string} num2
 * @return {string}
 */
const multiply = function (num1, num2) {
    if (num1 === "0" || num2 === "0")
        return "0";

    const result = Array.from({ length: num1.length + num2.length }).fill(0);
    const code_0 = "0".charCodeAt(0);

    const num1_len = num1.length;
    const num2_len = num2.length;

    for (let i = 0; i < num1_len; ++i) {
        const multiplier_1 = num1.charCodeAt(num1_len - i - 1) - code_0;
        for (let j = 0; j < num2_len; ++j) {
            const multiplier_2 = num2.charCodeAt(num2_len - j - 1) - code_0;
            result[i + j] += multiplier_1 * multiplier_2;
        }
    }

    result.reduce((carry, value, index) => {
        const sum = carry + value;
        result[index] = sum % 10;
        return sum / 10 | 0;
    }, 0);

    return result
        .slice(0, result.findLastIndex((d) => d !== 0) + 1)
        .reverse()
        .join("");
};
// @lc code=end

export default multiply;
