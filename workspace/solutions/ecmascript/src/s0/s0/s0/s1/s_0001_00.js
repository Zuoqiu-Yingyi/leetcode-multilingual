/*
 * @lc app=leetcode.cn id=1 lang=javascript
 *
 * [1] 两数之和
 */

// @lc code=start
/**
 * @param {number[]} nums
 * @param {number} target
 * @return {[number, number] | void}
 */
const twoSum = function (nums, target) {
    const map = {};
    for (let i = 0; i < nums.length; i++) {
        const num = nums[i];
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
