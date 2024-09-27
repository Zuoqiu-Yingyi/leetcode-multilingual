/*
 * @lc app=leetcode.cn id=1499 lang=javascript
 *
 * [1499] 满足不等式的最大值
 */

// @lc code=start
/**
 * @param {number[][]} points
 * @param {number} k
 * @return {number}
 */
const findMaxValueOfEquation = function (points, k) {
    let result = -Infinity;
    /**
     * @type {number[]}
     */
    const queue = [];

    points.forEach(([x, y], j) => {
        // @ts-ignore
        while (queue.length > 0 && (x - points[queue[0]][0]) > k) {
            queue.shift();
        }

        if (queue.length > 0) {
            const i = queue.at(0);
            // @ts-ignore
            result = Math.max(result, points[i][1] - points[i][0] + x + y);
        }

        // @ts-ignore
        const temp = y - x;
        while (queue.length > 0) {
            const i = queue.at(-1);
            // @ts-ignore
            if ((points[i][1] - points[i][0]) <= temp) {
                queue.pop();
            }
            else {
                break;
            }
        }

        queue.push(j);
    });

    return result;
};
// @lc code=end

export default findMaxValueOfEquation;
