/*
 * @lc app=leetcode.cn id=1856 lang=javascript
 *
 * [1856] 子数组最小乘积的最大值
 */

// @lc code=start
const MOD = BigInt(1e9 + 7);
/**
 * @param {number[]} nums
 * @return {number}
 */
const maxSumMinProduct = function (nums) {
    const n = nums.length;
    const left = Array.from({ length: n });
    const right = Array.from({ length: n });
    const stack = [];

    for (let i = 0; i < n; i++) {
        // @ts-ignore
        while (stack.length && nums[stack.at(-1)] >= nums[i]) {
            stack.pop();
        }
        left[i] = stack.at(-1) ?? -1;
        stack.push(i);
    }

    stack.length = 0;
    for (let i = n - 1; i >= 0; i--) {
        // @ts-ignore
        while (stack.length && nums[stack.at(-1)] > nums[i]) {
            stack.pop();
        }
        right[i] = stack.at(-1) ?? n;
        stack.push(i);
    }

    const prefix_sums = Array.from({ length: n + 1 });
    prefix_sums[0] = 0;
    for (let i = 0; i < n; i++) {
        prefix_sums[i + 1] = prefix_sums[i] + nums[i];
    }

    const result = nums.reduce((result, num, i) => {
        const product = BigInt(prefix_sums[right[i]] - prefix_sums[left[i] + 1]) * BigInt(num);
        return result > product ? result : product;
    }, 0n);
    return Number(result % MOD);
};
// @lc code=end

export default maxSumMinProduct;
