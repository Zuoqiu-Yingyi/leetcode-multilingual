/*
 * @lc app=leetcode.cn id=304 lang=java
 *
 * [304] 二维区域和检索 - 矩阵不可变
 */

package s0.s3.s0.s4.s_0304_00;

// @lc code=start
class NumMatrix {
    private final int n;
    private final int m;
    private final int[][] matrix;
    private final int[][] prefix_sums_matrix;
    private boolean initialized = false;

    public NumMatrix(
        int[][] matrix
    ) {
        this.n = matrix.length;
        this.m = matrix[0].length;
        this.matrix = matrix;
        this.prefix_sums_matrix = new int[this.n + 1][this.m + 1];
    }

    public int sumRegion(
        int row1, int col1, int row2, int col2
    ) {
        this.init();
        return this.prefix_sums_matrix[row2 + 1][col2 + 1]
            - this.prefix_sums_matrix[row2 + 1][col1]
            - this.prefix_sums_matrix[row1][col2 + 1]
            + this.prefix_sums_matrix[row1][col1];
    }

    private boolean init() {
        if (this.initialized) {
            return false;
        }

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.m; j++) {
                this.prefix_sums_matrix[i + 1][j + 1] //
                    = this.prefix_sums_matrix[i + 1][j]
                    + this.prefix_sums_matrix[i][j + 1]
                    - this.prefix_sums_matrix[i][j]
                    + this.matrix[i][j];
            }
        }

        this.initialized = true;
        return true;
    }
}

/**
 * Your NumMatrix object will be instantiated and called as such:
 * NumMatrix obj = new NumMatrix(matrix);
 * int param_1 = obj.sumRegion(row1,col1,row2,col2);
 */
// @lc code=end

public class s_0304_00 extends NumMatrix {
    public s_0304_00(
        int[][] matrix
    ) {
        super(matrix);
    }
}
