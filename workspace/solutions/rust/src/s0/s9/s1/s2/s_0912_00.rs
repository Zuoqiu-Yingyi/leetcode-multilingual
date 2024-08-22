/*
 * @lc app=leetcode.cn id=912 lang=rust
 *
 * [912] 排序数组
 */

pub struct Solution {}

// @lc code=start
impl Solution {
    pub fn sort_array(mut nums: Vec<i32>) -> Vec<i32> {
        let n = nums.len();
        /* 排序整个区间的元素 */
        Self::quick_sort(&mut nums, 0, n - 1);
        return nums;
    }

    /// 快速排序
    fn quick_sort(
        nums: &mut Vec<i32>,
        left: usize,
        right: usize,
    ) -> () {
        /*
        当 left >= right 时
            区间内元素个数小于等于 1
            无需排序
         */
        if left >= right {
            return;
        }

        let mut l = left as isize - 1;
        let mut r = right as isize + 1;

        /* 取待排序的第一个元素为分治阈值 */
        let pivot = nums[left];

        /*
        当 l >= r 时
            说明已经遍历完整个区间
            无需再次排序
         */
        while l < r {
            /* 在左侧找到比阈值大的一个元素 */
            loop {
                l += 1;
                if nums[l as usize] >= pivot {
                    break;
                }
            }

            /* 在右侧找到比阈值小的一个元素 */
            loop {
                r -= 1;
                if nums[r as usize] <= pivot {
                    break;
                }
            }

            /*
            交换左侧与右侧的元素
            目标:
                l 左侧的元素都小于等于 pivot
                r 右侧的元素都大于等于 pivot
             */
            if l < r {
                nums.swap(l as usize, r as usize);
            }
        }

        /*
        由于 r <= l
        且 l 左侧的元素 <= pivot
            因此区间 [left, r] 的元素 <= pivot
        区间 (r, right] 的元素 >= pivot
         */
        Self::quick_sort(nums, left, r as usize);
        Self::quick_sort(nums, r as usize + 1, right);
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::sort_array;
