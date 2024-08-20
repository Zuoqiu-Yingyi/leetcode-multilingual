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

import importlib
from importlib.machinery import ModuleSpec
import json
import pathlib
import re
from typing import List

from .example import Example

ID_LENGTH = 4
SOLUTION_FILE_NAME_REGEXP = re.compile(r"^s_\d{4}_\d{2}\.py$")
EXAMPLES_DIRECTORY_PATH = pathlib.Path.cwd() / "./../../packages/examples"


def t(spec: ModuleSpec) -> None:
    # 获取题解模块路径列表
    directory = pathlib.Path(spec.origin).parent
    solution_file_paths = [entry for entry in directory.iterdir() if entry.is_file() and SOLUTION_FILE_NAME_REGEXP.match(entry.name) is not None]
    assert len(solution_file_paths) > 0, "No solutions"

    # 加载示例文件
    paths = [
        *map(
            lambda s: s.removeprefix("s"),
            directory.parts[-ID_LENGTH:],
        )
    ]
    id = "".join(paths)
    example_path = EXAMPLES_DIRECTORY_PATH / "/".join(paths) / f"{id}.json"
    assert example_path.is_file(), f"No example file: {example_path}"

    examples: List[Example] = json.loads(example_path.read_text())
    assert len(examples) > 0, "No examples"

    # 遍历题解模块
    for solution_index, solution_file_path in enumerate(solution_file_paths):
        module = importlib.import_module(f"src.{'.'.join(solution_file_path.parts[-ID_LENGTH - 1:-1])}.{solution_file_path.stem}")
        solution = module.Solution()
        methods = [method_name for method_name in dir(solution) if callable(getattr(solution, method_name)) and not method_name.startswith("_")]
        assert len(methods) > 0, "No solution method"
        assert len(methods) == 1, "Too many solution methods"
        method = getattr(solution, methods[0])

        # 遍历示例
        for example_index, example in enumerate(examples):
            input = example["input"]
            output = example["output"]
            result = method(*input)
            assert result == output, "\n".join(
                [
                    "",
                    f"solution: {solution_index}",
                    f"example:  {example_index}",
                    f"input:    {input}",
                    f"result:   {result}",
                    f"expected: {output}",
                    "",
                ]
            )
