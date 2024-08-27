# Copyright (C) 2024 Zuoqiu Yingyi
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.

from typing import Any, List, TypedDict


class Example(TypedDict):
    input: List[Any]
    output: Any


class ExampleWithInit(TypedDict):
    init: List[Any]
    inputs: List[List[Any]]
    outputs: List[Any]


class ExampleIO:
    input: List[Any]
    output: Any

    def __init__(self, input: List[Any], output: Any):
        self.input = input
        self.output = output


class ExamplesSet:
    init: List[Any]
    examples: List[ExampleIO]

    def __init__(self, init: List[Any] | None = None, examples: List[ExampleIO] | None = None):
        self.init = init if init else []
        self.examples = examples if examples else []

    @staticmethod
    def listOf(examples: List[Example | ExampleWithInit]) -> List["ExamplesSet"]:
        examples_set_list = []
        examples_set = ExamplesSet()
        for example in examples:
            if "init" in example and "inputs" in example and "outputs" in example:
                init = example.get("init")
                inputs = example.get("inputs")
                outputs = example.get("outputs")

                assert init is not None, "init is None"
                assert inputs is not None, "inputs is None"
                assert outputs is not None, "outputs is None"

                assert len(inputs) == len(outputs), "len(inputs) != len(outputs)"
                examples_set_list.append(ExamplesSet(init, map(lambda io: ExampleIO(*io), zip(inputs, outputs))))
            elif "input" in example and "output" in example:
                input = example.get("input")
                output = example.get("output")

                assert input is not None, "input is None"
                assert output is not None, "output is None"

                examples_set.examples.append(ExampleIO(input, output))
        if len(examples_set.examples) > 0:
            examples_set_list.append(examples_set)
        return examples_set_list
