#[cfg(test)]
mod tests {
    use super::super::s_0303_00;
    use super::super::s_0303_01;
    use crate::{
        s0::s3::s0::s3::ISolution,
        utils::{json::ValueExtension, test::t_},
    };

    #[test]
    fn test() -> () {
        t_(
            module_path!(), //
            &vec![0, 1],
            |solution, init| -> Box<dyn ISolution> {
                let nums = init[0].to_i32_array().unwrap();
                match solution {
                    0 => Box::new(s_0303_00::NumArray::init(nums)),
                    1 => Box::new(s_0303_01::NumArray::init(nums)),
                    _ => unreachable!(),
                }
            },
            |solution, input| {
                serde_json::Value::from(solution.solve(
                    input[0].to_i32().unwrap(), //
                    input[1].to_i32().unwrap(),
                ))
            },
        );
    }
}
