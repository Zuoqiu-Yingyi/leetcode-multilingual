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

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.double
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import kotlin.io.path.Path
import kotlin.test.*

public class Example {
    public lateinit var input: Array<Any>
    public lateinit var output: Any

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
        this.input = List(inputJsonArray.size) { index ->
            Example.json2any(inputJsonArray.get(index), parameterTypeNames[index])
        }.toTypedArray()
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
        this.output = Example.json2any(outputJsonObject, returnTypeName)
    }

    companion object {
        public val SOLUTIONS_TEST_EXAMPLES_DIRECTORY: String = "./../../../packages/examples"

        /**
         * 将包名转换为题解 ID
         * @param packageName 包名 (格式: "_0._1._2._3")
         * @return 题解 ID (格式: 0123)
         */
        public final fun package2id(packageName: String): String = packageName
            .replace("s", "")
            .replace(".", "")

        /**
         * 将包名转换为路径
         * @param packageName 包名 (格式: "_0._1._2._3")
         * @return 路径 (格式: "0/1/2/3")
         */
        public final fun package2path(packageName: String): String = packageName
            .replace("s", "")
            .replace(".", "/")

        /**
         * 读取示例文件
         * @param packageName 包名 (格式: "_0._1._2._3")
         * @return JSON 字符串
         */
        public final fun readExamplesFile(packageName: String): String? {
            try {
                val id = Example.package2id(packageName)
                val path = Example.package2path(packageName)
                val file_path = Path(
                    Example.SOLUTIONS_TEST_EXAMPLES_DIRECTORY,
                    path,
                    "$id.json",
                )
                val json = File(file_path.toString()).readText()
                return json
            } catch (e: Exception) {
                return null
            }
        }

        /**
         * 创建示例列表
         * @param json 示例 JSON 字符串
         * @param parameterTypeNames 方法参数类型名称列表
         * @param returnTypeName 方法返回值类型名称
         * @return 示例列表
         */
        public final fun fromJson(
            json: String,
            parameterTypeNames: List<String>,
            returnTypeName: String,
        ): List<Example> {
            val examples_json_array = Json.parseToJsonElement(json).jsonArray
            val examples = examples_json_array.map { example_json_element ->
                val example_json_object = example_json_element.jsonObject
                Example().apply {
                    this.setInput(example_json_object.get("input")?.jsonArray, parameterTypeNames)
                    this.setOutput(example_json_object.get("output"), returnTypeName)
                }
            }
            return examples
        }

        /**
         * 将 JSON 元素转换为指定类型
         * @param jsonElement JSON 元素
         * @param typeName 类型名称
         * @return 指定类型
         *
         * @see [kotlinx.serialization.json](https://kotlinlang.org/api/kotlinx.serialization/kotlinx-serialization-json/kotlinx.serialization.json/)
         */
        public final fun json2any(
            jsonElement: JsonElement,
            typeName: String,
        ): Any = when (typeName) {
            "kotlin.String" -> jsonElement.jsonPrimitive.content
            "kotlin.Int" -> jsonElement.jsonPrimitive.int
            "kotlin.Double" -> jsonElement.jsonPrimitive.double
            "kotlin.IntArray" -> jsonElement.jsonArray.map { it.jsonPrimitive.int }.toIntArray()
            else -> throw IllegalArgumentException("Unsupported type (json2any): $typeName")
        }

        /**
         * 将 Any 类型的值转换为指定类型
         * @param value 任意类型的值
         * @param typeName 类型名称
         * @return 指定类型
         */
        public final fun any2json(
            value: Any,
            typeName: String,
        ): JsonElement = when (typeName) {
            "kotlin.String" -> Json.encodeToJsonElement(value as String)
            "kotlin.Int" -> Json.encodeToJsonElement(value as Int)
            "kotlin.Double" -> Json.encodeToJsonElement(value as Double)
            "kotlin.IntArray" -> Json.encodeToJsonElement(value as IntArray)
            else -> throw IllegalArgumentException("Unsupported type (any2json): $typeName")
        }
    }
}
