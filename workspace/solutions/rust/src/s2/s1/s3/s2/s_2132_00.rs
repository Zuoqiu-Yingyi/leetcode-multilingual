/*
 * @lc app=leetcode.cn id=2132 lang=rust
 *
 * [2132] 用邮票贴满网格图
 */

use std::vec;

pub struct Solution {}

// @lc code=start
impl Solution {
    pub fn possible_to_stamp(
        grid: Vec<Vec<i32>>,
        stamp_height: i32,
        stamp_width: i32,
    ) -> bool {
        let stamp_height = stamp_height as usize;
        let stamp_width = stamp_width as usize;

        let m = grid.len();
        let n = grid[0].len();

        /*
        前缀和矩阵
        通过该矩阵可以判断指定区域内的元素和 (区域内被占据的格子数量)
        */
        let mut prefix_sums_matrix = vec![vec![0; n + 1]; m + 1];

        /* prefix_sums_matrix 元素 (i+1, j+1) 中保存 grid 矩阵从 (0, 0) 到 (i, j) 的分块中被占据的格子数量 */
        for i in 0..m {
            for j in 0..n {
                #[rustfmt::skip]
                let sum
                    = prefix_sums_matrix[i][j + 1]
                    + prefix_sums_matrix[i + 1][j]
                    - prefix_sums_matrix[i][j]
                    + grid[i][j]; // 该格子被占据时为 1
                prefix_sums_matrix[i + 1][j + 1] = sum;
            }
        }

        /*
        差分矩阵
        将邮票覆盖视为该区域所有格子值 +1
        通过计算差分矩阵可以判断指定区域内的元素和 (区域内被占据的格子数量 + 邮票覆盖的格子数量)
         */
        let mut diff_matrix = vec![vec![0; n + 1]; m + 1];
        for i in 0..m {
            for j in 0..n {
                /* 当前格子被占据 */
                if grid[i][j] != 0 {
                    continue;
                }

                /* 以 (i, j) 为邮票左上角时邮票的 下边界 & 右边界 */
                let bottom = i + stamp_height;
                let right = j + stamp_width;

                /* 判断在 (i, j) 处张贴邮票是否超出区域边界 */
                if bottom > m || right > n {
                    continue;
                }

                /* 判断在 (i, j) 处张贴邮票是否会盖住被占据的格子 */
                #[rustfmt::skip]
                let sum
                    = prefix_sums_matrix[bottom][right]
                    - prefix_sums_matrix[bottom][j]
                    - prefix_sums_matrix[i][right]
                    + prefix_sums_matrix[i][j];
                if sum == 0 {
                    /* 邮票不会覆盖被占据的格子 */

                    /*
                    参考一维差分数组
                        给区间 [l,..r] 中的每一个数加上 c，那么有 d[l] += c，并且 d[r+1] −= c
                        对差分数组求前缀和，即可得到原数组
                     */
                    diff_matrix[i][j] += 1;
                    diff_matrix[bottom][right] += 1;

                    diff_matrix[i][right] -= 1;
                    diff_matrix[bottom][j] -= 1;
                }
            }
        }

        /* 格子覆盖邮票层数矩阵 */
        let mut sum_matrix = vec![vec![0; n + 1]; m + 1];

        /* 遍历所有格子, 判断该格子是否 被占据/被贴邮票 */
        for i in 0..m {
            for j in 0..n {
                /* 判断该格子是否被占据 */
                if grid[i][j] != 0 {
                    /* 格子被占据, 该格子上一定不会粘贴邮票 */
                    continue;
                }

                /* 该格子上方邮票层数 */
                #[rustfmt::skip]
                let sum
                    = sum_matrix[i][j + 1]
                    + sum_matrix[i + 1][j]
                    - sum_matrix[i][j]
                    + diff_matrix[i][j];

                /* 判断该格子上是否被贴邮票 */
                if sum == 0 {
                    /* 该格子上未被贴邮票 */
                    return false;
                }

                sum_matrix[i + 1][j + 1] = sum;
            }
        }

        true
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::possible_to_stamp;
