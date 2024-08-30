#[cfg(test)]
mod tests {
    use super::super::s_0304_00;
    use crate::{
        s0::s3::s0::s4::ISolution,
        utils::{json::ValueExtension, test::t_},
    };

    #[test]
    fn test() -> () {
        t_(
            module_path!(), //
            &vec![0],
            |solution, init| -> Box<dyn ISolution> {
                let matrix = init[0].to_i32_matrix().unwrap();
                match solution {
                    0 => Box::new(s_0304_00::NumMatrix::init(matrix)),
                    _ => unreachable!(),
                }
            },
            |solution, input| {
                serde_json::Value::from(solution.solve(
                    input[0].to_i32().unwrap(), //
                    input[1].to_i32().unwrap(),
                    input[2].to_i32().unwrap(),
                    input[3].to_i32().unwrap(),
                ))
            },
        );
    }
}
