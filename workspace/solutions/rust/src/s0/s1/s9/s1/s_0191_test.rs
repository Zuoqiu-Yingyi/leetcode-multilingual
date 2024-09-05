#[cfg(test)]
mod tests {
    use super::super::s_0191_00;
    use super::super::s_0191_01;
    use crate::utils::{json::ValueExtension, test::t};

    #[test]
    fn test() -> () {
        t(
            module_path!(), //
            &vec![
                s_0191_00::SOLUTION, //
                s_0191_01::SOLUTION,
            ],
            |solve, input| {
                serde_json::Value::from(solve(
                    input[0].to_i32().unwrap(), //
                ))
            },
        );
    }
}
