#[cfg(test)]
mod tests {
    use super::super::s_1898_00;
    use crate::utils::{json::ValueExtension, test::t};

    #[test]
    fn test() -> () {
        t(
            module_path!(), //
            &vec![s_1898_00::SOLUTION],
            |solve, input| {
                serde_json::Value::from(solve(
                    input[0].as_str().unwrap().to_string(), //
                    input[1].as_str().unwrap().to_string(), //
                    input[2].to_i32_array().unwrap(),       //
                ))
            },
        );
    }
}
