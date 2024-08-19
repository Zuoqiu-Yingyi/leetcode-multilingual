// Copyright (C) 2024 Zuoqiu Yingyi
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

use super::example;

const ID_LENGTH: usize = 4;

pub fn parse_module_path(module_path: &str) -> Vec<&str> {
    module_path.split("::").collect::<Vec<&str>>()[1..=ID_LENGTH]
        .iter()
        .map(|s| s.strip_prefix("s").unwrap())
        .collect()
}

/**
- `module_path`: 入口模块的模块路径
  - 示例: `solutions::s0::s0::s0::s1::s_0001_test::tests`
- `solutions`: 解决方案的列表
- `call`: 调用测试函数
 */
pub fn t<S>(
    module_path: &str,
    solutions: &Vec<S>,
    call: fn(&S, &Vec<serde_json::Value>) -> serde_json::Value,
) -> () {
    assert!(solutions.len() > 0, "No solutions");

    let paths = parse_module_path(module_path);
    let example_file_path = example::get_examples_file_path(&paths);
    let json = example::read_examples_file(&example_file_path).unwrap();
    let examples = example::deserialize_examples_json(&json);

    assert!(examples.len() > 0, "No examples");

    for (solution_index, solution) in solutions.iter().enumerate() {
        for (example_index, example) in examples.iter().enumerate() {
            let input = &example.input;
            let output = &example.output;
            let result = call(solution, input);
            assert_eq!(
                &result,
                output,
                "\n\
                solution: {solution_index}\n\
                example:  {example_index}\n\
                input:    {input}\n\
                result:   {result}\n\
                expected: {output}\n\
                ",
                solution_index = solution_index,
                example_index = example_index,
                input = serde_json::to_string(input).unwrap(),
                result = serde_json::to_string(&result).unwrap(),
                output = serde_json::to_string(output).unwrap(),
            );
        }
    }
}
