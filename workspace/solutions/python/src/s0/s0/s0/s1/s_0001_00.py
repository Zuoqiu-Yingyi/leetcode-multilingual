#
# @lc app=leetcode.cn id=1 lang=python3
#
# [1] 两数之和
#

from typing import List


# @lc code=start
class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        map = {}
        for i, num in enumerate(nums):
            diff = target - num
            j = map.get(diff)
            if j is not None:
                return [j, i]
            map[num] = i


# @lc code=end
