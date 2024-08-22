/*
 * @lc app=leetcode.cn id=1 lang=rust
 *
 * [1] 两数之和
 */

pub struct Solution {}

// @lc code=start
use std::collections::HashMap;

impl Solution {
    pub fn two_sum(
        nums: Vec<i32>,
        target: i32,
    ) -> Vec<i32> {
        let mut map = HashMap::new();
        for (i, &num) in nums.iter().enumerate() {
            /* num 与 diff 共同组成 target */
            let diff = target - num;

            /* 查询哈希表 map 中 diff 是否存在 */
            if let Some(&j) = map.get(&diff) {
                /* 若 diff 存在则即为答案 */
                return vec![j as i32, i as i32];
            }

            /*
            diff 不存在
                diff 在数组 nums 中, 但此时还未将其加入 map
                    将 num 加入 map, 之后扫描到 diff 时可以从字典中获取 num, 进而得到答案
                若 diff 不在数组 nums 中
                    将 num 加入 map 无副作用, 其可能为另一个 num 的匹配的 diff
            */
            map.insert(num, i as i32);
        }
        unreachable!()
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::two_sum;
