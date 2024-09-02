/*
 * @lc app=leetcode.cn id=304 lang=javascript
 *
 * [304] 二维区域和检索 - 矩阵不可变
 */

// @lc code=start
/**
 * @param {number[][]} matrix
 * @constructor
 */
function NumMatrix(matrix) {
    this.n = matrix.length;
    // @ts-ignore
    this.m = matrix[0].length;
    this.matrix = matrix;
    this.prefix_sums_matrix = Array
        .from({ length: this.n + 1 })
        .fill(undefined)
        .map(() => Array
            .from({ length: this.m + 1 })
            .fill(0),
        );
    this.initialized = false;
};

/**
 * @param {number} row1
 * @param {number} col1
 * @param {number} row2
 * @param {number} col2
 * @return {number}
 */
NumMatrix.prototype.sumRegion = function (row1, col1, row2, col2) {
    this.init();
    // @ts-ignore
    return this.prefix_sums_matrix[row2 + 1][col2 + 1]
        // @ts-ignore
        - this.prefix_sums_matrix[row2 + 1][col1]
        // @ts-ignore
        - this.prefix_sums_matrix[row1][col2 + 1]
        // @ts-ignore
        + this.prefix_sums_matrix[row1][col1];
};

/**
 * @return {boolean}
 */
NumMatrix.prototype.init = function () {
    if (!this.initialized) {
        for (let i = 0; i < this.n; i++) {
            for (let j = 0; j < this.m; j++) {
                // @ts-ignore
                this.prefix_sums_matrix[i + 1][j + 1]
                    // @ts-ignore
                    = this.prefix_sums_matrix[i + 1][j]
                    // @ts-ignore
                    + this.prefix_sums_matrix[i][j + 1]
                    // @ts-ignore
                    - this.prefix_sums_matrix[i][j]
                    // @ts-ignore
                    + this.matrix[i][j];
            }
        }

        this.initialized = true;
        return true;
    }
    return false;
};

/**
 * Your NumMatrix object will be instantiated and called as such:
 * var obj = new NumMatrix(matrix)
 * var param_1 = obj.sumRegion(row1,col1,row2,col2)
 */
// @lc code=end

NumMatrix.prototype.func = NumMatrix.prototype.sumRegion;
export default NumMatrix;
