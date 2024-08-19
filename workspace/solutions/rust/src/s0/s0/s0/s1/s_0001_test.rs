#[cfg(test)]
mod tests {
    use super::super::s_0001_00;

    #[test]
    fn test() {
        assert_eq!(
            vec![0, 1],
            s_0001_00::Solution::two_sum(vec![2, 7, 11, 15], 9)
        );
    }
}
