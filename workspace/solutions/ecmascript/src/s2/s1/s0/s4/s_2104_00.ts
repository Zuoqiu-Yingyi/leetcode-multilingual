/*
 * @lc app=leetcode.cn id=2104 lang=typescript
 *
 * [2104] 子数组范围和
 */

// @lc code=start
function subArrayRanges(nums: number[]): number {
    const n = nums.length;
    let result = 0;
    for (let i = 0; i < n - 1; i++) {
        let min = nums[i]!;
        let max = nums[i]!;
        for (let j = i + 1; j < n; j++) {
            min = Math.min(min, nums[j]!);
            max = Math.max(max, nums[j]!);
            result += max - min;
        }
    }
    return result;
}
// @lc code=end

export default subArrayRanges;
