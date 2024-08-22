/*
 * @lc app=leetcode.cn id=34 lang=rust
 *
 * [34] 在排序数组中查找元素的第一个和最后一个位置
 */

pub struct Solution {}

// @lc code=start
impl Solution {
    pub fn search_range(
        nums: Vec<i32>,
        target: i32,
    ) -> Vec<i32> {
        let n = nums.len();
        let search = |x| {
            let mut left = 0;
            let mut right = n;
            /* nums[left] <= x <= nums[right] */
            while left < right {
                /*
                由于 left < right
                    因此 middle ∈ [left, right) 中
                    right == n 时也不会出现数组越界
                 */
                let middle = (left + right) >> 1;
                if nums[middle] < x {
                    /*
                    nums[middle] < x
                    若 nums[middle + 1] < x 时
                        不知道搜索的 x 是否存在
                        依然满足 nums[middle + 1] <= x <= nums[right]
                    若 nums[middle + 1] == x 时
                        可知搜索的 x 存在
                        不会再次进入该分支
                        * left 会停留在该位置
                        由于 nums[middle] < x
                        * 因此 left 即为答案
                    若 nums[middle + 1] > x 时
                        可知搜索的 x 不存在
                        下一轮搜索区间为 [middle + 1, right)
                        由于 x < nums[middle + 1] <= nums[right]
                            不会再次进入该分支
                            * left 会停留在大于 x 的第一个位置
                    ---
                    当 left 停止移动时
                        若 left < right
                            则循环继续, right 会继续减小, 直到 right <= left 循环结束
                        若 left >= right
                            * 则循环结束, 返回 left
                    ---
                    由于 middle ∈ [left, right)
                        因此 left <= middle
                        因此 left < middle + 1
                        因此 left = middle + 1 导致 left 只会增加，不会减少
                     */
                    left = middle + 1;
                } else {
                    /*
                    x <= nums[middle]
                    ---
                    由于 middle ∈ [left, right)
                        因此 middle < right
                        因此 right = middle 导致 right 只会减少，不会增加
                     */
                    right = middle;
                }
            }
            /*
            若目标 x 存在
                left 为目标 x 的位置
            若目标 x 不存在
                left 为比 x 大的第一个位置
             */
            left
        };

        /*
        若 target 存在
            则 l 为最左侧的 target 的位置
        若 target 不存在
            则 l 为比 target 大的第一个位置
         */
        let l = search(target);

        /*
        若 target 存在
            则 r 为最右侧的 target 的后一个位置
        若 target 不存在
            则 r 为比 target 大的第一个位置
         */
        let r = search(target + 1);

        /*
        若 target 存在
            则 l 为最左侧的 target 的位置
            r 为最右侧的 target 的后一个位置
            此时 l < r
        若 target 不存在
            则 l 与 r 均为比 target 大的第一个位置
            此时 l == r
         */
        if l < r {
            return vec![l as i32, (r - 1) as i32];
        } else {
            return vec![-1, -1];
        }
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::search_range;
