package _0001

import (
	"testing"

	"github.com/Zuoqiu-Yingyi/leetcode-multilingual/workspace/solutions/go/utils"

	"github.com/Zuoqiu-Yingyi/leetcode-multilingual/workspace/solutions/go/_0/_0/_0/_1/s_0001_01"
)

type Solution = func([]int, int) []int

func TestSolutions(t *testing.T) {
	utils.Test(
		[]Solution{
			s_0001_01.Solution,
		},
		t,
	)
}
