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

import (
	"encoding/json"
	"fmt"
	"os"
	"path"
	"reflect"
	"strings"
)

const (
	ID_LENGTH                         = 4
	PATH_SEPARATOR                    = string(os.PathSeparator)
	SOLUTIONS_TEST_EXAMPLES_DIRECTORY = "./../../packages/examples"
)

func Map(arr []string, f func(string) string) []string {
	arr_ := make([]string, len(arr))
	for i, v := range arr {
		arr_[i] = f(v)
	}
	return arr_
}

// GetProblemInfoFromCWD 获取当前工作目录下的题目信息
func GetProblemInfoFromCWD() (root, id string, err error) {
	var cwd string
	if cwd, err = os.Getwd(); err != nil {
		return
	}

	fullPaths := strings.Split(cwd, PATH_SEPARATOR)
	rootPaths := fullPaths[:len(fullPaths)-ID_LENGTH]
	solutionsPaths := fullPaths[len(fullPaths)-ID_LENGTH:]
	paths := Map(solutionsPaths, func(s string) string {
		return strings.TrimLeft(s, "s_")
	})

	root = strings.Join(rootPaths, "/")
	id = strings.Join(paths, "")
	return
}

type Example struct {
	Input  []any `json:"input"`
	Output any   `json:"output"`
}

// loadExamples 从 JSON 文件中加载测试示例
func loadExamples() (examples []*Example, err error) {
	var root, id string
	if root, id, err = GetProblemInfoFromCWD(); err != nil {
		return
	}

	fmt.Printf("%#v", root)

	/* 计算测试示例文件路径 */
	paths := strings.Split(id, "")
	examplesPath := strings.Join(paths, "/")
	examplesFilePath := path.Join(root, SOLUTIONS_TEST_EXAMPLES_DIRECTORY, examplesPath, id+".json")

	/* 判断文件是否存在 */
	if _, err = os.Stat(examplesFilePath); err != nil {
		return
	}

	/* 读取文件内容 */
	var content []byte
	if content, err = os.ReadFile(examplesFilePath); err != nil {
		return
	}

	/* JSON 文件反序列化 */
	if err = json.Unmarshal(content, &examples); err != nil {
		return
	}

	return
}

func GetExamples() (examples []*Example, err error) {
	examples, err = loadExamples()
	return
}

type ExampleValue struct {
	Inputs  []reflect.Value
	Outputs []reflect.Value
}

func ExamplesToExamplesValue(examples []*Example, inputTypes, outputTypes []*reflect.Type) (examplesValue []*ExampleValue, err error) {
	examplesValue = make([]*ExampleValue, len(examples))
	for i, example := range examples {
		if len(example.Input) != len(inputTypes) {
			panic("invalid example input length")
		}
		if len(outputTypes) != 1 {
			panic("invalid solution output length")
		}

		Inputs := make([]reflect.Value, len(inputTypes))
		for j, input := range example.Input {
			Inputs[j], err = AnyToReflectValue(input, *inputTypes[j])
			if err != nil {
				return
			}
		}

		Outputs := make([]reflect.Value, 1)
		Outputs[0], err = AnyToReflectValue(example.Output, *outputTypes[0])
		if err != nil {
			return
		}

		examplesValue[i] = &ExampleValue{
			Inputs:  Inputs,
			Outputs: Outputs,
		}
	}
	return
}
