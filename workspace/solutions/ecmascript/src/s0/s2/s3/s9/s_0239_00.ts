/*
 * @lc app=leetcode.cn id=239 lang=typescript
 *
 * [239] 滑动窗口最大值
 */

// @lc code=start
function maxSlidingWindow(nums: number[], k: number): number[] {
    const queue: number[] = [];
    const result: number[] = [];

    for (let i = 0; i < nums.length; i++) {
        const num = nums[i]!;
        if (queue.length > 0 && (i + 1 - k) > queue.at(0)!) {
            queue.shift();
        }
        while (queue.length > 0 && nums[queue.at(-1)!]! <= num) {
            queue.pop();
        }
        queue.push(i);
        if ((i + 1) >= k) {
            result.push(nums[queue.at(0)!]!);
        }
    }
    return result;
}
// @lc code=end

export default maxSlidingWindow;
