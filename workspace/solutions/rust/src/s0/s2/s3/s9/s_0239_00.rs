/*
 * @lc app=leetcode.cn id=239 lang=rust
 *
 * [239] 滑动窗口最大值
 */

pub struct Solution {}

// @lc code=start
use std::collections::VecDeque;

/* 单调队列 */
impl Solution {
    pub fn max_sliding_window(
        nums: Vec<i32>,
        k: i32,
    ) -> Vec<i32> {
        /*
        单调递减队列 (堆首是最大值)
            保存 nums 的下标 index
            nums[queue[i]] 单调递减
         */
        let mut queue: VecDeque<usize> = VecDeque::new();
        let mut result: Vec<i32> = vec![];

        for (i, num) in nums.iter().enumerate() {
            /*
            i: 窗口右边界元素
            判断队列首元素 (最大值) 是否滑出窗口
                (i + 1 - k): 滑动窗口左边界下标
                queue.front(): 最大值下标
            窗口宽度 < k 时 (i + 1 - k) < 0 <= queue.front()
                无副作用
            滑出窗口后需要移除该元素
             */
            if !queue.is_empty() && ((i as i32) + 1 - k) > (*queue.front().unwrap() as i32) {
                queue.pop_front();
            }

            /*
            队列递减
                严格单调递减:
                    新添加的同值队首元素下标更大, 移除时更晚移除, 不影响最终结果
                不严格单调递减:
                    队首多个元素值相同时, 不影响最终结果
             */
            while !queue.is_empty() && nums[*queue.back().unwrap()] <= *num {
                queue.pop_back();
            }

            /*
            队列中所有元素值都比当前元素大
            满足单调性
             */
            queue.push_back(i);

            if (i + 1) >= (k as usize) {
                /*
                窗口完全展开
                队首元素为窗口中元素的最大值
                 */
                result.push(nums[*queue.front().unwrap()]);
            }
        }

        result
    }
}
// @lc code=end

pub const SOLUTION: super::TSolution = Solution::max_sliding_window;
