/*
 * @lc app=leetcode.cn id=2104 lang=javascript
 *
 * [2104] 子数组范围和
 */

// @lc code=start
/**
 * @param {number[]} nums
 * @return {number}
 */
const subArrayRanges = function (nums) {
    const n = nums.length;
    let result = 0;
    for (let i = 0; i < n - 1; i++) {
        let min = nums[i] ?? 0;
        let max = nums[i] ?? 0;
        for (let j = i + 1; j < n; j++) {
            min = Math.min(min, nums[j] ?? 0);
            max = Math.max(max, nums[j] ?? 0);
            result += max - min;
        }
    }
    return result;
};
// @lc code=end

export default subArrayRanges;
