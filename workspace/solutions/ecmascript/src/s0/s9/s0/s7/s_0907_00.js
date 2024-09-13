/*
 * @lc app=leetcode.cn id=907 lang=javascript
 *
 * [907] 子数组的最小值之和
 */

// @lc code=start
const MOD = 1e9 + 7;
/**
 * @param {number[]} arr
 * @return {number}
 */
const sumSubarrayMins = function (arr) {
    const n = arr.length;
    const stack = [];
    const left = Array.from({ length: n });
    const right = Array.from({ length: n });

    for (let i = 0; i < n; i++) {
        // @ts-ignore
        while (stack.length > 0 && arr[stack.at(-1)] >= arr[i]) {
            stack.pop();
        }
        left[i] = stack.at(-1) ?? -1;
        stack.push(i);
    }

    stack.length = 0;
    for (let i = n - 1; i >= 0; i--) {
        // @ts-ignore
        while (stack.length > 0 && arr[stack.at(-1)] > arr[i]) {
            stack.pop();
        }
        right[i] = stack.at(-1) ?? n;
        stack.push(i);
    }

    let result = 0;
    for (let i = 0; i < n; i++) {
        // @ts-ignore
        result += arr[i] * (i - left[i]) * (right[i] - i);
        result %= MOD;
    }
    return result;
};
// @lc code=end

export default sumSubarrayMins;
