/*
 * @lc app=leetcode.cn id=962 lang=typescript
 *
 * [962] 最大宽度坡
 */

// @lc code=start
function maxWidthRamp(nums: number[]): number {
    const n = nums.length;
    const stack: number[] = [];

    for (let i = 0; i < n; i++) {
        if (stack.length === 0 || nums[stack.at(-1)!]! > nums[i]!) {
            stack.push(i);
        }
    }

    let result = 0;
    for (let j = n - 1; j >= 0; j--) {
        while (stack.length > 0 && nums[stack.at(-1)!]! <= nums[j]!) {
            result = Math.max(result, j - stack.pop()!);
        }
        if (stack.length === 0) {
            break;
        }
    }
    return result;
}
// @lc code=end

export default maxWidthRamp;
