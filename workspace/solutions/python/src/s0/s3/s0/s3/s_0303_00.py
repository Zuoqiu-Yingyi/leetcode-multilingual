#
# @lc app=leetcode.cn id=303 lang=python3
#
# [303] 区域和检索 - 数组不可变
#

from typing import List
from itertools import accumulate


# @lc code=start
class NumArray:
    def __init__(self, nums: List[int]):
        self.prefix_sums = list(accumulate(nums, initial=0))

    def sumRange(self, left: int, right: int) -> int:
        return self.prefix_sums[right + 1] - self.prefix_sums[left]


# Your NumArray object will be instantiated and called as such:
# obj = NumArray(nums)
# param_1 = obj.sumRange(left,right)
# @lc code=end

Solution = NumArray
