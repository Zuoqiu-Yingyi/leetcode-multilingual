/*
 * @lc app=leetcode.cn id=304 lang=rust
 *
 * [304] 二维区域和检索 - 矩阵不可变
 */

// @lc code=start
type TMatrix = Vec<Vec<i32>>;

pub struct NumMatrix {
    /** 原矩阵的行数 */
    n: usize,
    /** 原矩阵的列数 */
    m: usize,

    /**
    原矩阵
        ⎡ 00 01 .. 0m ⎤
        ⎢ 10 11 .. 1m ⎥
        ⎢ .. .. .. .. ⎥
        ⎣ n0 n1 .. im ⎦
     */
    matrix: TMatrix,

    /**
    前缀和矩阵
    其中 prefix_sums_matrix[i + 1][j + 1] 表示下方矩阵内所有元素的和
        ⎡ 00 01 .. 0j ⎤
        ⎢ 10 11 .. 1j ⎥
        ⎢ .. .. .. .. ⎥
        ⎣ i0 i1 .. ij ⎦
     */
    prefix_sums_matrix: TMatrix,

    /**
     * 前缀和矩阵是否已初始化
     */
    initialized: bool,
}

/**
 * `&self` means the method takes an immutable reference.
 * If you need a mutable reference, change it to `&mut self` instead.
 */
impl NumMatrix {
    fn new(matrix: TMatrix) -> Self {
        /* 矩阵的行数 */
        let n = matrix.len();

        /* 矩阵的列数 */
        let m = matrix[0].len();

        NumMatrix {
            n,
            m,
            matrix,
            /*
            为确保 i=0 且 j=0 时前缀和矩阵中相应的元素为 0
            前缀和矩阵的行数和列数均比原矩阵多 1
             */
            prefix_sums_matrix: vec![vec![0; m + 1]; n + 1],
            initialized: false,
        }
    }

    fn sum_region(
        &mut self,
        row1: i32,
        col1: i32,
        row2: i32,
        col2: i32,
    ) -> i32 {
        self.init();

        let row1 = row1 as usize;
        let col1 = col1 as usize;
        let row2 = row2 as usize;
        let col2 = col2 as usize;

        #[rustfmt::skip]
        let sum
            = self.prefix_sums_matrix[row2 + 1][col2 + 1]
            - self.prefix_sums_matrix[row2 + 1][col1]
            - self.prefix_sums_matrix[row1][col2 + 1]
            + self.prefix_sums_matrix[row1][col1];
        sum
    }

    /**
     * 初始化二维前缀和矩阵
     */
    fn init(&mut self) -> bool {
        if self.initialized {
            return false;
        }

        for i in 0..self.n {
            for j in 0..self.m {
                /*
                原矩阵
                ⎡     00      01  ..      0(j-1)      0j ⎤
                ⎢     10      11  ..      1(j-1)      1j ⎥
                ⎢     ..      ..  ..          ..      .. ⎥
                ⎢ (i-1)0  (i-1)1  ..  (i-1)(j-1)  (i-1)j ⎥
                ⎣     i0      i1  ..      i(j-1)      ij ⎦
                 */
                #[rustfmt::skip]
                let sum
                    = self.prefix_sums_matrix[i + 1][j]
                    + self.prefix_sums_matrix[i][j + 1]
                    - self.prefix_sums_matrix[i][j]
                    + self.matrix[i][j];

                self.prefix_sums_matrix[i + 1][j + 1] = sum
            }
        }

        self.initialized = true;
        return true;
    }
}

/**
 * Your NumMatrix object will be instantiated and called as such:
 * let obj = NumMatrix::new(matrix);
 * let ret_1: i32 = obj.sum_region(row1, col1, row2, col2);
 */
// @lc code=end

impl super::ISolution for NumMatrix {
    fn init(matrix: TMatrix) -> impl super::ISolution {
        NumMatrix::new(matrix)
    }

    fn solve(
        &mut self,
        row1: i32,
        col1: i32,
        row2: i32,
        col2: i32,
    ) -> i32 {
        self.sum_region(row1, col1, row2, col2)
    }
}
