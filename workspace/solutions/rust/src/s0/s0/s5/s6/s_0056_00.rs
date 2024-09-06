/*
 * @lc app=leetcode.cn id=56 lang=rust
 *
 * [56] 合并区间
 */

pub struct Solution {}

// @lc code=start
impl Solution {
    pub fn merge(mut intervals: Vec<Vec<i32>>) -> Vec<Vec<i32>> {
        /* 按照左端点升序排序 */
        intervals.sort_by(|a, b| a[0].cmp(&b[0]));
        let mut result = vec![];
        let mut i = 0;
        let n = intervals.len();
        while i < n {
            let left = intervals[i][0];
            let mut right = intervals[i][1];
            loop {
                i += 1;
                /* 前一个右端点大于等于后一个左端点时说明两个区间重合 */
                if i < n && right >= intervals[i][0] {
                    /* 重合区间右端点取更大的值 */
                    right = right.max(intervals[i][1]);
                } else {
                    /* 两个区间不重合 */
                    result.push(vec![left, right]);
                    break;
                }
            }
        }
        result
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::merge;
