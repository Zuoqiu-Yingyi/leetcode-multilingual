pub mod s_0304_00;
pub mod s_0304_test;

pub trait ISolution {
    fn init(matrix: Vec<Vec<i32>>) -> impl ISolution
    where
        Self: Sized;

    fn solve(
        &mut self,
        row1: i32,
        col1: i32,
        row2: i32,
        col2: i32,
    ) -> i32;
}
