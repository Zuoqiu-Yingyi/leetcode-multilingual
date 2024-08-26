/*
* @lc app=leetcode.cn id=303 lang=javascript
*
* [303] 区域和检索 - 数组不可变
*/

// @lc code=start
/**
 * @param {number[]} nums
 * @constructor
 */
function NumArray(nums) {
    this.prefix_sums = nums.slice();
    this.prefix_sums.reduce((sum, value, index, sums) => {
        sum += value;
        sums[index] = sum;
        return sum;
    }, 0);
};

/**
 * @param {number} left
 * @param {number} right
 * @return {number}
 */
NumArray.prototype.sumRange = function (left, right) {
    return (this.prefix_sums[right] ?? 0) - (this.prefix_sums[left - 1] ?? 0);
};

/**
 * Your NumArray object will be instantiated and called as such:
 * var obj = new NumArray(nums)
 * var param_1 = obj.sumRange(left,right)
 */
// @lc code=end

NumArray.prototype.func = NumArray.prototype.sumRange;
export default NumArray;
