/*
 * @lc app=leetcode.cn id=768 lang=javascript
 *
 * [768] 最多能完成排序的块 II
 */

// @lc code=start
/**
 * @param {number[]} arr
 * @return {number}
 */
const maxChunksToSorted = function (arr) {
    const stack = [];
    for (const num of arr) {
        if (stack.length === 0 || stack.at(-1) <= num) {
            stack.push(num);
        }
        else {
            const max = stack.pop();
            while (stack.length > 0 && num < stack.at(-1)) {
                stack.pop();
            }
            stack.push(max);
        }
    }
    return stack.length;
};
// @lc code=end

export default maxChunksToSorted;
