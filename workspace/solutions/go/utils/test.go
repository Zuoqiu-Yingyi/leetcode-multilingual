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

func Test[T any](solutions []T, t *testing.T) {
	if len(solutions) == 0 {
		t.Fatal("No solutions")
	}

	examples, err := GetExamples()
	if err != nil {
		t.Fatal(err)
	}
	if len(examples) == 0 {
		t.Fatal("No examples")
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

	examplesValue, err := ExamplesToExamplesValue(examples, inputTypes, outputTypes)
	if err != nil {
		t.Fatal(err)
	}

	for s, solution := range solutions {
		solutionValue := reflect.ValueOf(solution)
		t.Run(fmt.Sprintf("solution:%d", s), func(t *testing.T) {
			for e, exampleValue := range examplesValue {
				t.Run(fmt.Sprintf("example:%d", e), func(t *testing.T) {
					outputs := ReflectValueToAny(solutionValue.Call(exampleValue.Inputs))
					expected := ReflectValueToAny(exampleValue.Outputs)
					if !reflect.DeepEqual(outputs, expected) {
						inputs := ReflectValueToAny(exampleValue.Inputs)
						t.Errorf(
							"solution: %d, example: %d, input: %v, output: %v, expected: %v",
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
