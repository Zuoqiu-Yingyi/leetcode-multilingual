/*
 * @lc app=leetcode.cn id=1 lang=typescript
 *
 * [1] 两数之和
 */

// @lc code=start
function twoSum(nums: number[], target: number): [number, number] | void {
    const map = {} as Record<number, number>;
    for (let i = 0; i < nums.length; i++) {
        const num = nums[i]!;
        const diff = target - num;
        const j = map[diff];
        if (j != null) {
            return [j, i];
        }
        map[num] = i;
    }
};
// @lc code=end

export default twoSum;
