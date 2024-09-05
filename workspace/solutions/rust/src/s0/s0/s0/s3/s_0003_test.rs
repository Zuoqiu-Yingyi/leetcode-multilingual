#[cfg(test)]
mod tests {
    use super::super::s_0003_00;
    use super::super::s_0003_01;
    use crate::utils::test::t;

    #[test]
    fn test() -> () {
        t(
            module_path!(), //
            &vec![
                s_0003_00::SOLUTION, //
                s_0003_01::SOLUTION,
            ],
            |solve, input| {
                serde_json::Value::from(solve(
                    input[0].as_str().unwrap().to_string(), //
                ))
            },
        );
    }
}
