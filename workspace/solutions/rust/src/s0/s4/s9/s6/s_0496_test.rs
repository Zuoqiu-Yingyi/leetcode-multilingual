#[cfg(test)]
mod tests {
    use super::super::s_0496_00;
    use crate::utils::{json::ValueExtension, test::t};

    #[test]
    fn test() -> () {
        t(
            module_path!(), //
            &vec![s_0496_00::SOLUTION],
            |solve, input| {
                serde_json::Value::from(solve(
                    input[0].to_i32_array().unwrap(), //
                    input[1].to_i32_array().unwrap(),
                ))
            },
        );
    }
}
