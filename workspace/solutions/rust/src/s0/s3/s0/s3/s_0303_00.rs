/*
 * @lc app=leetcode.cn id=303 lang=rust
 *
 * [303] 区域和检索 - 数组不可变
 */

// @lc code=start
pub struct NumArray {
    prefix_sums: Vec<i32>,
}

/**
 * `&self` means the method takes an immutable reference.
 * If you need a mutable reference, change it to `&mut self` instead.
 */
impl NumArray {
    pub fn new(nums: Vec<i32>) -> Self {
        let nums_len = nums.len();
        let mut prefix_sums = vec![0_i32; nums_len + 1];
        for i in 0..nums_len {
            prefix_sums[i + 1] = prefix_sums[i] + nums[i];
        }
        Self {
            prefix_sums,
        }
    }

    pub fn sum_range(
        &self,
        left: i32,
        right: i32,
    ) -> i32 {
        self.prefix_sums[right as usize + 1] - self.prefix_sums[left as usize]
    }
}

/*
 * Your NumArray object will be instantiated and called as such:
 * let obj = NumArray::new(nums);
 * let ret_1: i32 = obj.sum_range(left, right);
 */
// @lc code=end

impl super::ISolution for NumArray {
    fn init(nums: Vec<i32>) -> impl super::ISolution {
        NumArray::new(nums)
    }

    fn solve(
        &self,
        left: i32,
        right: i32,
    ) -> i32 {
        self.sum_range(left, right)
    }
}
