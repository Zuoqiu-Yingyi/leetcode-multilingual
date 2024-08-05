package _0001

import (
	"testing"

	"github.com/Zuoqiu-Yingyi/leetcode-multilingual/workspace/solutions/go/utils"

	_0001_01 "github.com/Zuoqiu-Yingyi/leetcode-multilingual/workspace/solutions/go/_0/_0/_0/_1/s_0001_01"
)

type Solution = func([]int, int) []int

type Example struct {
	Input struct {
		Num    []int
		Target int
	}
	Output []int
}

var (
	examples  = UnmarshalExamples(utils.GetExamples())
	solutions = []Solution{
		_0001_01.TwoSum,
	}
)

func UnmarshalExamples(examples []*utils.Example) []*Example {
	res := make([]*Example, len(examples))
	for i, example := range examples {
		res[i] = &Example{}
		res[i].Input.Num = utils.ToIntArray(example.Input[0].([]any))
		res[i].Input.Target = utils.ToInt(example.Input[1])
		res[i].Output = utils.ToIntArray(example.Output.([]any))
	}
	return res
}

func TestTwoSum(t *testing.T) {
	if len(examples) == 0 {
		t.Fatal("No examples")
	}

	for _, solution := range solutions {
		for _, example := range examples {
			output := solution(example.Input.Num, example.Input.Target)
			if !utils.IntArrayEqual(output, example.Output) {
				t.Errorf("input: %v, output: %v, expected: %v", example.Input, output, example.Output)
			}
		}
	}
}
