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

package utils

func ToInt(num any) int {
	switch num := num.(type) {
	case float64:
		return int(num)
	case int:
		return num
	default:
		panic("invalid type")
	}
}

func ToIntArray(arr []any) []int {
	res := make([]int, len(arr))
	for i, v := range arr {
		res[i] = ToInt(v)
	}
	return res
}

func ToStringArray(arr []interface{}) []string {
	res := make([]string, len(arr))
	for i, v := range arr {
		res[i] = v.(string)
	}
	return res
}
