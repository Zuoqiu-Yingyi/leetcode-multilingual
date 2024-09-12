#[cfg(test)]
mod tests {
    use super::super::s_0739_00;
    use crate::utils::{json::ValueExtension, test::t};

    #[test]
    fn test() -> () {
        t(
            module_path!(), //
            &vec![s_0739_00::SOLUTION],
            |solve, input| {
                serde_json::Value::from(solve(
                    input[0].to_i32_array().unwrap(), //
                ))
            },
        );
    }
}
