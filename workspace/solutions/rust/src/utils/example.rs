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

use std::{
    io::Read,
    path::{PathBuf, MAIN_SEPARATOR_STR},
};

static EXAMPLES_DIRECTORY_PATH: &[&str] = &["..", "..", "packages", "examples"];

#[derive(Debug)]
pub struct Example {
    pub input: Vec<serde_json::Value>,
    pub output: serde_json::Value,
}

pub fn read_examples_file(file_path: &PathBuf) -> Result<String, std::io::Error> {
    let mut file = std::fs::File::open(file_path)?;
    let mut json = String::new();
    file.read_to_string(&mut json)?;
    Ok(json)
}

pub fn get_examples_file_path(paths: &Vec<&str>) -> PathBuf {
    let id: String = paths.join("");
    std::env::current_dir() //
        .unwrap()
        .join(EXAMPLES_DIRECTORY_PATH.join(MAIN_SEPARATOR_STR))
        .join(paths.join(MAIN_SEPARATOR_STR))
        .join(format!("{}.json", id))
}

pub fn deserialize_examples_json(json: &str) -> Vec<Example> {
    if let serde_json::Value::Array(arr) = serde_json::from_str(json).unwrap() {
        arr.iter()
            .map(|v| {
                let input = v["input"].as_array().unwrap().to_vec();
                let output = v["output"].clone();
                Example {
                    input,
                    output,
                }
            })
            .collect()
    } else {
        panic!("examples json is not an array");
    }
}
