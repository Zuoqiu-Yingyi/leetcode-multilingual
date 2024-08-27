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

@file:Suppress("ktlint:standard:no-wildcard-imports")

package utils

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlin.test.*

public class Example {
    public lateinit var input: Array<Any>
    public lateinit var output: Any

    constructor() {}

    constructor(
        input: Array<Any>,
        output: Any,
    ) {
        this.input = input
        this.output = output
    }

    /**
     * 设置题解示例输入
     * @param inputJsonArray 示例输入 JSON 对象
     * @param parameterTypeNames 参数类型名称列表
     */
    public final fun setInput(
        inputJsonArray: JsonArray?,
        parameterTypeNames: List<String>,
    ) {
        assertNotNull(
            inputJsonArray,
            "The example.input is null",
        )
        assertEquals(
            inputJsonArray.size,
            parameterTypeNames.size,
            "The example.input.size != parameter.size $inputJsonArray $parameterTypeNames",
        )
        this.input = ExampleSet.json2array(inputJsonArray, parameterTypeNames)
    }

    /**
     * 设置题解示例输出
     * @param outputJsonObject 示例输出 JSON 对象
     * @param returnTypeName 返回值类型名称
     */
    public final fun setOutput(
        outputJsonObject: JsonElement?,
        returnTypeName: String,
    ) {
        assertNotNull(
            outputJsonObject,
            "The example.output is null",
        )
        this.output = ExampleSet.json2any(outputJsonObject, returnTypeName)
    }
}
