#[cfg(test)]
mod tests {
    use super::super::s_1499_00;
    use super::super::s_1499_01;
    use crate::utils::{json::ValueExtension, test::t};

    #[test]
    fn test() -> () {
        t(
            module_path!(), //
            &vec![
                s_1499_00::SOLUTION, //
                s_1499_01::SOLUTION,
            ],
            |solve, input| {
                serde_json::Value::from(solve(
                    input[0].to_i32_matrix().unwrap(), //
                    input[1].to_i32().unwrap(),
                ))
            },
        );
    }
}
