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
    /**
     * @param {number[]} nums
     */
    function f(nums) {
        const n = nums.length;
        /**
         * @type {number[]}
         */
        const stack = [];
        const left = Array.from({ length: n });
        const right = Array.from({ length: n });

        nums.forEach((num, i) => {
            // @ts-ignore
            while (stack.length > 0 && nums[stack.at(-1)] <= num) {
                stack.pop();
            }
            left[i] = stack.at(-1) ?? -1;
            stack.push(i);
        });

        stack.length = 0;
        nums.toReversed().forEach((num, i) => {
            const j = n - i - 1;
            // @ts-ignore
            while (stack.length > 0 && nums[stack.at(-1)] < num) {
                stack.pop();
            }
            right[j] = stack.at(-1) ?? n;
            stack.push(j);
        });

        return nums.reduce((result, num, i) => {
            // @ts-ignore
            return result + (i - left[i]) * (right[i] - i) * num;
        }, 0);
    }

    const max = f(nums);
    const min = f(nums.map((x) => -x));
    return max + min;
};
// @lc code=end

export default subArrayRanges;
