/*
 * @lc app=leetcode.cn id=304 lang=typescript
 *
 * [304] 二维区域和检索 - 矩阵不可变
 */

// @lc code=start
type TMatrix = number[][];

class NumMatrix {
    private readonly n: number;
    private readonly m: number;
    private readonly matrix: TMatrix;
    private readonly prefix_sums_matrix: TMatrix;
    private initialized: boolean;

    constructor(matrix: TMatrix) {
        this.n = matrix.length;
        this.m = matrix[0]!.length;
        this.matrix = matrix;
        this.prefix_sums_matrix = Array
            .from({ length: this.n + 1 })
            .fill(undefined)
            .map(() => Array
                .from({ length: this.m + 1 })
                .fill(0),
            ) as TMatrix;
        this.initialized = false;
    }

    sumRegion(row1: number, col1: number, row2: number, col2: number): number {
        this.init();
        return this.prefix_sums_matrix[row2 + 1]![col2 + 1]!
            - this.prefix_sums_matrix[row2 + 1]![col1]!
            - this.prefix_sums_matrix[row1]![col2 + 1]!
            + this.prefix_sums_matrix[row1]![col1]!;
    }

    private init(): boolean {
        if (!this.initialized) {
            for (let i = 0; i < this.n; i++) {
                for (let j = 0; j < this.m; j++) {
                    this.prefix_sums_matrix[i + 1]![j + 1]
                        = this.prefix_sums_matrix[i + 1]![j]!
                        + this.prefix_sums_matrix[i]![j + 1]!
                        - this.prefix_sums_matrix[i]![j]!
                        + this.matrix[i]![j]!;
                }
            }

            this.initialized = true;
            return true;
        }
        return false;
    }
}

/**
 * Your NumMatrix object will be instantiated and called as such:
 * var obj = new NumMatrix(matrix)
 * var param_1 = obj.sumRegion(row1,col1,row2,col2)
 */
// @lc code=end

export default class extends NumMatrix {
    public func = this.sumRegion;
    constructor(matrix: TMatrix) {
        super(matrix);
    }
};
