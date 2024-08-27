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
	"fmt"
	"reflect"
	"testing"
)

func T[T any](solutions []T, t *testing.T) {
	if len(solutions) == 0 {
		t.Fatal("No solutions")
	}

	funcType, err := GetFuncType(solutions[0])
	if err != nil {
		t.Fatal(err)
	}

	/* 校验函数签名是否一致 */
	funcTypeName := funcType.String()
	for _, solution := range solutions {
		funcType, err := GetFuncType(solution)
		if err != nil {
			t.Fatal(err)
		}
		_funcTypeName := funcType.String()
		if funcTypeName != _funcTypeName {
			t.Fatalf("Different function types: %s, %s", funcTypeName, _funcTypeName)
		}
	}

	inputTypes := GetFuncInputTypes(funcType)
	outputTypes := GetFuncOutputTypes(funcType)

	if IsTypesStruct(outputTypes) {
		structType := *outputTypes[0]
		if method, success := structType.MethodByName("Func"); success {
			methodType := method.Type
			methodInputTypes := GetFuncInputTypes(methodType)
			methodOutputTypes := GetFuncOutputTypes(methodType)

			examples, err := GetExamples[ExampleClass]()
			if err != nil {
				t.Fatal(err)
			}
			if len(examples) == 0 {
				t.Fatal("No examples")
			}

			examplesClassValue, err := ExamplesToExamplesClassValue(examples, inputTypes, methodInputTypes, methodOutputTypes, t)
			if err != nil {
				t.Fatal(err)
			}

			for s, solution := range solutions {
				solutionValue := reflect.ValueOf(solution)
				t.Run(fmt.Sprintf("solution:%d", s), func(t *testing.T) {
					for e, exampleClassValue := range examplesClassValue {
						object := solutionValue.Call(exampleClassValue.InitList)[0]
						t.Run(fmt.Sprintf("example:%d", e), func(t *testing.T) {
							for i, inputList := range exampleClassValue.InputLists {
								t.Run(fmt.Sprintf("input:%d", i), func(t *testing.T) {
									t.Logf("inputList len: %v", len(inputList))
									outputs := ReflectValueToAny(method.Func.Call(append([]reflect.Value{object}, inputList...)))
									expected := ReflectValueToAny(exampleClassValue.OutputLists[i])

									if !reflect.DeepEqual(outputs, expected) {
										inputs := ReflectValueToAny(inputList)
										t.Errorf(
											"\n"+
												"solution:  %d\n"+
												"examples:  %d\n"+
												"example:   %d\n"+
												"arguments: %v\n"+
												"result:    %v\n"+
												"expected:  %v\n"+
												"\n",
											s,
											e,
											i,
											inputs,
											outputs,
											expected,
										)
									}
								})
							}
						})
					}
				})
			}
		} else {
			t.Errorf("Method [Func] not found")
		}
	} else {

		examples, err := GetExamples[ExampleFunction]()
		if err != nil {
			t.Fatal(err)
		}
		if len(examples) == 0 {
			t.Fatal("No examples")
		}

		examplesValue, err := ExamplesToExamplesValue(examples, inputTypes, outputTypes, t)
		if err != nil {
			t.Fatal(err)
		}

		for s, solution := range solutions {
			solutionValue := reflect.ValueOf(solution)
			t.Run(fmt.Sprintf("solution:%d", s), func(t *testing.T) {
				for e, exampleValue := range examplesValue {
					t.Run(fmt.Sprintf("example:%d", e), func(t *testing.T) {
						outputs := ReflectValueToAny(solutionValue.Call(exampleValue.InputList))
						expected := ReflectValueToAny(exampleValue.OutputList)

						if !reflect.DeepEqual(outputs, expected) {
							inputs := ReflectValueToAny(exampleValue.InputList)
							t.Errorf(
								"\n"+
									"solution: %d\n"+
									"example:  %d\n"+
									"input:    %v\n"+
									"result:   %v\n"+
									"expected: %v\n"+
									"\n",
								s,
								e,
								inputs,
								outputs,
								expected,
							)
						}
					})
				}
			})
		}
	}
}
