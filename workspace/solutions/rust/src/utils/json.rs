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

pub trait ValueExtension {
    fn to_i32(&self) -> Option<i32>;
    fn to_i32_array(&self) -> Option<Vec<i32>>;
}

impl ValueExtension for serde_json::Value {
    fn to_i32(&self) -> Option<i32> {
        match self.as_i64() {
            Some(v) => Some(v as i32),
            None => None,
        }
    }

    fn to_i32_array(&self) -> Option<Vec<i32>> {
        match self.as_array() {
            Some(arr) => Some(arr.iter().map(to_i32).collect()),
            None => None,
        }
    }
}

pub fn to_i32(value: &serde_json::Value) -> i32 {
    value.as_i64().unwrap() as i32
}
