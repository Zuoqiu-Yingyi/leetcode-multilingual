pub mod s_0303_00;
pub mod s_0303_01;
pub mod s_0303_test;

pub trait ISolution {
    fn init(nums: Vec<i32>) -> impl ISolution
    where
        Self: Sized;

    fn solve(
        &self,
        left: i32,
        right: i32,
    ) -> i32;
}
