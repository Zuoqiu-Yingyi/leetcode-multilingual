/*
 * @lc app=leetcode.cn id=2104 lang=typescript
 *
 * [2104] 子数组范围和
 */

// @lc code=start
function subArrayRanges(nums: number[]): number {
    function f(nums: number[]): number {
        const n = nums.length;
        const stack: number[] = [];
        const left = Array.from<number>({ length: n });
        const right = Array.from<number>({ length: n });

        nums.forEach((num, i) => {
            while (stack.length > 0 && nums[stack.at(-1)!]! <= num) {
                stack.pop();
            }
            left[i] = stack.at(-1) ?? -1;
            stack.push(i);
        });

        stack.length = 0;
        for (let i = n - 1; i >= 0; i--) {
            const num = nums[i]!;
            while (stack.length > 0 && nums[stack.at(-1)!]! < num) {
                stack.pop();
            }
            right[i] = stack.at(-1) ?? n;
            stack.push(i);
        }

        return nums.reduce((result, num, i) => {
            return result + (i - left[i]!) * (right[i]! - i) * num;
        }, 0);
    }

    const max = f(nums);
    const min = f(nums.map((x) => -x));
    return max + min;
};
// @lc code=end

export default subArrayRanges;
