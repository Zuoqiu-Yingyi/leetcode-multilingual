#[cfg(test)]
mod tests {
    use super::super::s_2132_00;
    use crate::utils::{json::ValueExtension, test::t};

    #[test]
    fn test() -> () {
        t(
            module_path!(), //
            &vec![s_2132_00::SOLUTION],
            |solve, input| serde_json::Value::from(solve(input[0].to_i32_matrix().unwrap(), input[1].to_i32().unwrap(), input[2].to_i32().unwrap())),
        );
    }
}
