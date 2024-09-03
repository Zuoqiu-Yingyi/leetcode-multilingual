#
# @lc app=leetcode.cn id=304 lang=python3
#
# [304] 二维区域和检索 - 矩阵不可变
#

from typing import List


# @lc code=start
class NumMatrix:
    n: int
    m: int
    matrix: List[List[int]]
    prefix_sums_matrix: List[List[int]]
    initialized: bool = False

    def __init__(self, matrix: List[List[int]]):
        self.n = len(matrix)
        self.m = len(matrix[0])
        self.matrix = matrix
        self.prefix_sums_matrix = [[0] * (self.m + 1) for _ in range(self.n + 1)]

    def sumRegion(self, row1: int, col1: int, row2: int, col2: int) -> int:
        self.__init()
        return (
            self.prefix_sums_matrix[row2 + 1][col2 + 1]  #
            - self.prefix_sums_matrix[row1][col2 + 1]  #
            - self.prefix_sums_matrix[row2 + 1][col1]  #
            + self.prefix_sums_matrix[row1][col1]
        )

    def __init(self) -> bool:
        if self.initialized:
            return False

        for i in range(self.n):
            for j in range(self.m):
                self.prefix_sums_matrix[i + 1][j + 1] = (
                    self.prefix_sums_matrix[i + 1][j]  #
                    + self.prefix_sums_matrix[i][j + 1]  #
                    - self.prefix_sums_matrix[i][j]  #
                    + self.matrix[i][j]
                )

        self.initialized = True
        return True


# Your NumMatrix object will be instantiated and called as such:
# obj = NumMatrix(matrix)
# param_1 = obj.sumRegion(row1,col1,row2,col2)
# @lc code=end

Solution = NumMatrix
