/*
 * @lc app=leetcode.cn id=304 lang=golang
 *
 * [304] 二维区域和检索 - 矩阵不可变
 */

package s_0304_00

// @lc code=start
type TMatrix = [][]int

type NumMatrix struct {
	n                int
	m                int
	matrix           TMatrix
	prefixSumsMatrix TMatrix
	initialized      bool
}

func Constructor(matrix [][]int) NumMatrix {
	n := len(matrix)
	m := len(matrix[0])
	prefixSumsMatrix := make([][]int, n+1)
	for i := range prefixSumsMatrix {
		prefixSumsMatrix[i] = make([]int, m+1)
	}

	return NumMatrix{
		n:                n,
		m:                m,
		matrix:           matrix,
		prefixSumsMatrix: prefixSumsMatrix,
		initialized:      false,
	}
}

func (this *NumMatrix) SumRegion(row1 int, col1 int, row2 int, col2 int) int {
	this.init()

	return this.prefixSumsMatrix[row2+1][col2+1] -
		this.prefixSumsMatrix[row2+1][col1] -
		this.prefixSumsMatrix[row1][col2+1] +
		this.prefixSumsMatrix[row1][col1]
}

func (this *NumMatrix) init() bool {
	if this.initialized {
		return false
	}

	for i := 0; i < this.n; i++ {
		for j := 0; j < this.m; j++ {
			this.prefixSumsMatrix[i+1][j+1] = this.prefixSumsMatrix[i+1][j] +
				this.prefixSumsMatrix[i][j+1] -
				this.prefixSumsMatrix[i][j] +
				this.matrix[i][j]
		}
	}

	this.initialized = true
	return true
}

/**
 * Your NumMatrix object will be instantiated and called as such:
 * obj := Constructor(matrix);
 * param_1 := obj.SumRegion(row1,col1,row2,col2);
 */
// @lc code=end

func (this NumMatrix) Func(row1 int, col1 int, row2 int, col2 int) int {
	return this.SumRegion(row1, col1, row2, col2)
}

var Solution = Constructor
