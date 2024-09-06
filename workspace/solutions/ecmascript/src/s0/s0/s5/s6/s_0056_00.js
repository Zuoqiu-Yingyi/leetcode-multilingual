/*
 * @lc app=leetcode.cn id=56 lang=javascript
 *
 * [56] 合并区间
 */

// @lc code=start
/**
 * @param {number[][]} intervals
 * @return {number[][]}
 */
const merge = function (intervals) {
    // @ts-expect-error
    intervals.sort((a, b) => a[0] - b[0]);
    const result = [];
    const n = intervals.length;
    let i = 0;
    while (i < n) {
        // @ts-expect-error
        const left = intervals[i][0];
        // @ts-expect-error
        let right = intervals[i][1];
        while (true) {
            i++;
            // @ts-expect-error
            if (i < n && right >= intervals[i][0]) {
                // @ts-expect-error
                right = Math.max(right, intervals[i][1]);
            }
            else {
                result.push([left, right]);
                break;
            }
        }
    }

    // @ts-expect-error
    return result;
};
// @lc code=end

export default merge;
