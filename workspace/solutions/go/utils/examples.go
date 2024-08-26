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
	"testing"
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

type ExampleFunction struct {
	Input  []any `json:"input"`
	Output any   `json:"output"`
}

type ExampleClass struct {
	Init    []any   `json:"init"`
	Inputs  [][]any `json:"inputs"`
	Outputs []any   `json:"outputs"`
}

// loadExamplesContent 加载 JSON 文件内容
func loadExamplesContent() (content []byte, err error) {
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
	content, err = os.ReadFile(examplesFilePath)

	return
}

// loadExamples 从 JSON 文件中加载测试示例 (函数格式)
func loadExamples[T ExampleFunction | ExampleClass]() (examples []*T, err error) {
	var content []byte
	if content, err = loadExamplesContent(); err != nil {
		return
	}

	/* JSON 文件反序列化 */
	if err = json.Unmarshal(content, &examples); err != nil {
		return
	}

	return
}

func GetExamples[T ExampleFunction | ExampleClass]() (examples []*T, err error) {
	examples, err = loadExamples[T]()
	return
}

type ExampleValue struct {
	InputList  []reflect.Value
	OutputList []reflect.Value
}

type ExampleClassValue struct {
	InitList    []reflect.Value
	InputLists  [][]reflect.Value
	OutputLists [][]reflect.Value
}

func ExamplesToExamplesValue(examples []*ExampleFunction, inputTypes, outputTypes []*reflect.Type, t *testing.T) (examplesValue []*ExampleValue, err error) {
	examplesValue = make([]*ExampleValue, len(examples))
	for i, example := range examples {
		if len(example.Input) != len(inputTypes) {
			t.Errorf("invalid example input length: %v != %v", len(example.Input), len(inputTypes))
		}
		if len(outputTypes) != 1 {
			t.Errorf("invalid solution output length: %v", len(outputTypes))
		}

		inputList := make([]reflect.Value, len(inputTypes))
		for j, input := range example.Input {
			inputList[j], err = AnyToReflectValue(input, *inputTypes[j])
			if err != nil {
				return
			}
		}

		outputList := make([]reflect.Value, 1)
		outputList[0], err = AnyToReflectValue(example.Output, *outputTypes[0])
		if err != nil {
			return
		}

		examplesValue[i] = &ExampleValue{
			InputList:  inputList,
			OutputList: outputList,
		}
	}
	return
}

func ExamplesToExamplesClassValue(examples []*ExampleClass, initTypes, inputTypes, outputTypes []*reflect.Type, t *testing.T) (examplesClassValue []*ExampleClassValue, err error) {
	examplesClassValue = make([]*ExampleClassValue, len(examples))
	for i, example := range examples {
		if len(example.Init) != len(initTypes) {
			t.Errorf("invalid example init length: %v != %v", len(example.Init), len(initTypes))
		}
		if len(example.Inputs) != len(example.Outputs) {
			t.Errorf("invalid example inputs/outputs length: %v != %v", len(example.Inputs), len(example.Outputs))
		}
		if len(outputTypes) != 1 {
			t.Errorf("invalid solution output length: %v", len(outputTypes))
		}

		/* initList: 构造函数参数列表 */
		initList := make([]reflect.Value, len(initTypes))
		for j, init := range example.Init {
			initList[j], err = AnyToReflectValue(init, *initTypes[j])
			if err != nil {
				return
			}
		}

		inputLists := make([][]reflect.Value, len(example.Inputs))
		for j, exampleInputs := range example.Inputs {
			if len(exampleInputs) != len(inputTypes)-1 {
				t.Errorf("invalid example input length: %v != %v", len(exampleInputs), len(inputTypes)-1)
			}

			/* inputList: 求解函数参数列表 */
			inputList := make([]reflect.Value, len(example.Inputs[j]))
			for k, exampleInput := range exampleInputs {
				inputList[k], err = AnyToReflectValue(exampleInput, *inputTypes[k+1])
				if err != nil {
					return
				}
			}
			inputLists[j] = inputList
		}

		outputLists := make([][]reflect.Value, len(example.Outputs))
		for j, exampleOutput := range example.Outputs {
			/* outputList: 求解函数返回值列表 */
			outputList := make([]reflect.Value, 1)
			outputList[0], err = AnyToReflectValue(exampleOutput, *outputTypes[0])
			if err != nil {
				return
			}
			outputLists[j] = outputList
		}

		examplesClassValue[i] = &ExampleClassValue{
			InitList:    initList,
			InputLists:  inputLists,
			OutputLists: outputLists,
		}
	}
	return
}
