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
)

func GetFuncType(f any) (funcType reflect.Type, err error) {
	funcType = reflect.TypeOf(f)

	for funcType.Kind() == reflect.Array ||
		funcType.Kind() == reflect.Slice ||
		funcType.Kind() == reflect.Ptr {
		funcType = funcType.Elem()
	}
	if funcType.Kind() != reflect.Func {
		err = fmt.Errorf("%s is not a function", funcType.Kind())
	}
	return
}

// GetFuncInputTypes returns the input types of a function.
func GetFuncInputTypes(funcType reflect.Type) (inputTypes []*reflect.Type) {
	inputTypes = make([]*reflect.Type, funcType.NumIn())
	for i := 0; i < funcType.NumIn(); i++ {
		inputType := funcType.In(i)
		inputTypes[i] = &inputType
	}
	return
}

// GetFuncOutputTypes returns the output types of a function.
func GetFuncOutputTypes(funcType reflect.Type) (outputTypes []*reflect.Type) {
	outputTypes = make([]*reflect.Type, funcType.NumOut())
	for i := 0; i < funcType.NumOut(); i++ {
		outputType := funcType.Out(i)
		outputTypes[i] = &outputType
	}
	return
}

func IsTypesStruct(types []*reflect.Type) bool {
	if len(types) > 0 {
		t := *(types[0])
		return t.Kind() == reflect.Struct
	}
	return false
}

func AnyToItem[T bool | string](value any) T {
	switch v := value.(type) {
	case T:
		return v
	default:
		panic("invalid type")
	}
}

func AnyToNumberItem[T int | int8 | int16 | int32 | int64 | uint | uint8 | uint16 | uint32 | uint64 | float32 | float64](value any) T {
	switch v := value.(type) {
	case float64:
		return T(v)
	default:
		panic("invalid type")
	}
}

func AnyToVector[T bool | string](array []any) []T {
	res := make([]T, len(array))
	for i, v := range array {
		res[i] = AnyToItem[T](v)
	}
	return res
}

func AnyToMatrix[T bool | string](matrix [][]any) [][]T {
	res := make([][]T, len(matrix))
	for i, row := range matrix {
		res[i] = AnyToVector[T](row)
	}
	return res
}

func AnyToNumberVector[T int | int8 | int16 | int32 | int64 | uint | uint8 | uint16 | uint32 | uint64 | float32 | float64](array []any) []T {
	res := make([]T, len(array))
	for i, v := range array {
		res[i] = AnyToNumberItem[T](v)
	}
	return res
}

func AnyToNumberMatrix[T int | int8 | int16 | int32 | int64 | uint | uint8 | uint16 | uint32 | uint64 | float32 | float64](matrix [][]any) [][]T {
	res := make([][]T, len(matrix))
	for i, row := range matrix {
		res[i] = AnyToNumberVector[T](row)
	}
	return res
}

func AnyToReflectValue(value any, valueType reflect.Type) (reflectValue reflect.Value, err error) {
	switch v := value.(type) {
	case bool:
		reflectValue = reflect.ValueOf(v)
	case string:
		reflectValue = reflect.ValueOf(v)
	case float64:
		switch valueType.Kind() {
		case reflect.Int:
			reflectValue = reflect.ValueOf(int(v))
		case reflect.Int8:
			reflectValue = reflect.ValueOf(int8(v))
		case reflect.Int16:
			reflectValue = reflect.ValueOf(int16(v))
		case reflect.Int32:
			reflectValue = reflect.ValueOf(int32(v))
		case reflect.Int64:
			reflectValue = reflect.ValueOf(int64(v))

		case reflect.Uint:
			reflectValue = reflect.ValueOf(uint(v))
		case reflect.Uint8:
			reflectValue = reflect.ValueOf(uint8(v))
		case reflect.Uint16:
			reflectValue = reflect.ValueOf(uint16(v))
		case reflect.Uint32:
			reflectValue = reflect.ValueOf(uint32(v))
		case reflect.Uint64:
			reflectValue = reflect.ValueOf(uint64(v))

		case reflect.Float32:
			reflectValue = reflect.ValueOf(float32(v))
		case reflect.Float64:
			reflectValue = reflect.ValueOf(float64(v))
		}
	case []any:
		switch valueType.Elem().Kind() {
		case reflect.Bool:
			reflectValue = reflect.ValueOf(AnyToVector[bool](v))
		case reflect.String:
			reflectValue = reflect.ValueOf(AnyToVector[string](v))

		case reflect.Int:
			reflectValue = reflect.ValueOf(AnyToNumberVector[int](v))
		case reflect.Int8:
			reflectValue = reflect.ValueOf(AnyToNumberVector[int8](v))
		case reflect.Int16:
			reflectValue = reflect.ValueOf(AnyToNumberVector[int16](v))
		case reflect.Int32:
			reflectValue = reflect.ValueOf(AnyToNumberVector[int32](v))
		case reflect.Int64:
			reflectValue = reflect.ValueOf(AnyToNumberVector[int64](v))

		case reflect.Uint:
			reflectValue = reflect.ValueOf(AnyToNumberVector[uint](v))
		case reflect.Uint8:
			reflectValue = reflect.ValueOf(AnyToNumberVector[uint8](v))
		case reflect.Uint16:
			reflectValue = reflect.ValueOf(AnyToNumberVector[uint16](v))
		case reflect.Uint32:
			reflectValue = reflect.ValueOf(AnyToNumberVector[uint32](v))
		case reflect.Uint64:
			reflectValue = reflect.ValueOf(AnyToNumberVector[uint64](v))

		case reflect.Float32:
			reflectValue = reflect.ValueOf(AnyToNumberVector[float32](v))
		case reflect.Float64:
			reflectValue = reflect.ValueOf(AnyToNumberVector[float64](v))

		default:
			err = fmt.Errorf("invalid type")
		}
	case [][]any:
		switch valueType.Elem().Elem().Kind() {
		case reflect.Bool:
			reflectValue = reflect.ValueOf(AnyToMatrix[bool](v))
		case reflect.String:
			reflectValue = reflect.ValueOf(AnyToMatrix[string](v))

		case reflect.Int:
			reflectValue = reflect.ValueOf(AnyToNumberMatrix[int](v))
		case reflect.Int8:
			reflectValue = reflect.ValueOf(AnyToNumberMatrix[int8](v))
		case reflect.Int16:
			reflectValue = reflect.ValueOf(AnyToNumberMatrix[int16](v))
		case reflect.Int32:
			reflectValue = reflect.ValueOf(AnyToNumberMatrix[int32](v))
		case reflect.Int64:
			reflectValue = reflect.ValueOf(AnyToNumberMatrix[int64](v))

		case reflect.Uint:
			reflectValue = reflect.ValueOf(AnyToNumberMatrix[uint](v))
		case reflect.Uint8:
			reflectValue = reflect.ValueOf(AnyToNumberMatrix[uint8](v))
		case reflect.Uint16:
			reflectValue = reflect.ValueOf(AnyToNumberMatrix[uint16](v))
		case reflect.Uint32:
			reflectValue = reflect.ValueOf(AnyToNumberMatrix[uint32](v))
		case reflect.Uint64:
			reflectValue = reflect.ValueOf(AnyToNumberMatrix[uint64](v))

		case reflect.Float32:
			reflectValue = reflect.ValueOf(AnyToNumberMatrix[float32](v))
		case reflect.Float64:
			reflectValue = reflect.ValueOf(AnyToNumberMatrix[float64](v))

		default:
			err = fmt.Errorf("invalid type")
		}
	default:
		err = fmt.Errorf("invalid type")
	}
	return
}

func ReflectValueToAny(values []reflect.Value) []any {
	res := make([]any, len(values))
	for i, value := range values {
		res[i] = value.Interface()
	}
	return res
}
