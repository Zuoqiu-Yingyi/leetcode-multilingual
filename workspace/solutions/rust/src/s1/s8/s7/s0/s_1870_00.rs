/*
 * @lc app=leetcode.cn id=1870 lang=rust
 *
 * [1870] 准时到达的列车最小时速
 */

pub struct Solution {}

// @lc code=start
impl Solution {
    pub fn min_speed_on_time(
        dist: Vec<i32>,
        hour: f64,
    ) -> i32 {
        /* 车次数量 */
        let n: usize = dist.len();

        /*
        由于列车只在整点发车
            因此列车车次大于整小时数时无法准时到达
         */
        if dist.len() > hour.ceil() as usize {
            return -1;
        }

        /*
        由于 time 小数点后最多存在两位数字
            则非零的小数部分最小值为 0.01 (最后一趟列车用时)
        又因为列车行驶距离最大为 10^5
            所以列车速度 >= 10^5 / 0.01 = 10^7 时一定能准点到达
         */
        const M: i32 = 10_000_000;

        /* 列车最慢速度 */
        let mut left = 1;

        /* 列车最快速度 */
        let mut right = M + 1;

        let distances: Vec<f64> = dist.iter().map(|&x| x as f64).collect();

        /* 判断能否准点到达 */
        let is_on_time = |speed: f64| -> bool {
            /* 非最后一程, 需要等下一趟整点发车的车次 */
            let time = distances[..n - 1] //
                .iter()
                .fold(
                    0_f64, //
                    |t, distance| t + (*distance / speed).ceil(),
                );

            /* 最后一程 */
            let time = time + distances[n - 1] / speed;

            /* 能否在时限内到达 */
            time <= hour
        };

        /* 找到满足条件的最小值, 用二分查找 */
        while left < right {
            let middle = (left + right) / 2;

            /* 判断能否准点到达 */
            if is_on_time(middle as f64) {
                /* 能准点到达, 搜索更慢的速度 */
                right = middle;
            } else {
                /* 不能准点到达, 搜索更快的速度 */
                left = middle + 1;
            }
        }
        return if left > M {
            -1
        } else {
            left
        };
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::min_speed_on_time;
