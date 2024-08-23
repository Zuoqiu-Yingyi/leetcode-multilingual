/*
 * @lc app=leetcode.cn id=912 lang=rust
 *
 * [912] 排序数组
 */

pub struct Solution {}

// @lc code=start
use std::collections::VecDeque;

struct SortRange {
    left: isize,
    right: isize,
}

impl Solution {
    pub fn sort_array(mut nums: Vec<i32>) -> Vec<i32> {
        Self::quick_sort(&mut nums);
        nums
    }

    /// 快速排序 (递推版本)
    fn quick_sort(nums: &mut Vec<i32>) -> () {
        let mut sort_ranges: VecDeque<SortRange> = VecDeque::new();
        sort_ranges.push_back(SortRange {
            left: 0,
            right: (nums.len() - 1) as isize,
        });

        while !sort_ranges.is_empty() {
            let SortRange {
                left,
                right,
            } = sort_ranges.pop_back().unwrap();
            if left >= right {
                continue;
            }

            let mut l = left as isize - 1;
            let mut r = right as isize + 1;
            let pivot = nums[left as usize];

            while l < r {
                loop {
                    l += 1;
                    if nums[l as usize] >= pivot {
                        break;
                    }
                }

                loop {
                    r -= 1;
                    if nums[r as usize] <= pivot {
                        break;
                    }
                }

                if l < r {
                    nums.swap(l as usize, r as usize);
                }
            }

            sort_ranges.push_back(SortRange {
                left,
                right: r,
            });
            sort_ranges.push_back(SortRange {
                left: r + 1,
                right,
            });
        }
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::sort_array;
