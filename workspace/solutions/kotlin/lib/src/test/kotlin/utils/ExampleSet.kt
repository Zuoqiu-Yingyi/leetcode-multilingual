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
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.double
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import java.io.File
import kotlin.io.path.Path
import kotlin.test.*

public class ExampleSet(
    public val init: Array<Any>,
    public val examples: List<Example>,
) {

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
                val id = ExampleSet.package2id(packageName)
                val path = ExampleSet.package2path(packageName)
                val file_path = Path(
                    ExampleSet.SOLUTIONS_TEST_EXAMPLES_DIRECTORY,
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
         * @param constructorParameterTypeNames 构造参数类型名称列表
         * @param methodParameterTypeNames 方法参数类型名称列表
         * @param methodReturnTypeName 方法返回值类型名称
         * @return 示例列表
         */
        public final fun fromJson(
            json: String,
            constructorParameterTypeNames: List<String>,
            methodParameterTypeNames: List<String>,
            methodReturnTypeName: String,
        ): List<ExampleSet> {
            val examples_json_array = Json.parseToJsonElement(json).jsonArray
            val exampleSetList: MutableList<ExampleSet> = mutableListOf()

            if (constructorParameterTypeNames.isNotEmpty()) {
                examples_json_array.forEach { example_json ->
                    val example_json_object = example_json.jsonObject
                    val init = ExampleSet.getInit(example_json_object.get("init")?.jsonArray, constructorParameterTypeNames)
                    val inputs = example_json_object.get("inputs")?.jsonArray
                    val outputs = example_json_object.get("outputs")?.jsonArray

                    assertNotNull(inputs, "example.inputs is Null")
                    assertNotNull(outputs, "example.outputs is Null")
                    assertEquals(
                        inputs.size,
                        outputs.size,
                        "example.inputs.size != example.outputs.size",
                    )

                    val example_size = inputs.size
                    val examples = (0..<example_size).map { i ->
                        Example().apply {
                            this.setInput(inputs[i].jsonArray, methodParameterTypeNames)
                            this.setOutput(outputs[i], methodReturnTypeName)
                        }
                    }
                    exampleSetList.addLast(ExampleSet(init, examples))
                }
            } else { // 构造函数无参数
                val examples = examples_json_array.map { example_json_element ->
                    val example_json_object = example_json_element.jsonObject
                    Example().apply {
                        this.setInput(example_json_object.get("input")?.jsonArray, methodParameterTypeNames)
                        this.setOutput(example_json_object.get("output"), methodReturnTypeName)
                    }
                }
                exampleSetList.addLast(ExampleSet(arrayOf(), examples))
            }
            return exampleSetList
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
            "kotlin.Boolean" -> jsonElement.jsonPrimitive.boolean
            "kotlin.Int" -> jsonElement.jsonPrimitive.int
            "kotlin.Long" -> jsonElement.jsonPrimitive.long
            "kotlin.Double" -> jsonElement.jsonPrimitive.double
            "kotlin.IntArray" -> jsonElement.jsonArray.map { it.jsonPrimitive.int }.toIntArray()
            "kotlin.Array<kotlin.IntArray>" -> jsonElement.jsonArray.map { it.jsonArray.map { it.jsonPrimitive.int }.toIntArray() }.toTypedArray()
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
            "kotlin.Boolean" -> Json.encodeToJsonElement(value as Boolean)
            "kotlin.Int" -> Json.encodeToJsonElement(value as Int)
            "kotlin.Long" -> Json.encodeToJsonElement(value as Long)
            "kotlin.Double" -> Json.encodeToJsonElement(value as Double)
            "kotlin.IntArray" -> Json.encodeToJsonElement(value as IntArray)
            "kotlin.Array<kotlin.IntArray>" -> Json.encodeToJsonElement(value as Array<IntArray>)
            else -> throw IllegalArgumentException("Unsupported type (any2json): $typeName")
        }

        /**
         * 将 JSON 元素转换为 Array 类型
         * @param jsonArray JSON 列表元素
         * @param typeNames 类型名称列表
         * @return 指定类型
         */
        public final fun json2array(
            jsonArray: JsonArray,
            typeNames: List<String>,
        ): Array<Any> = List(jsonArray.size) { index ->
            ExampleSet.json2any(jsonArray.get(index), typeNames[index])
        }.toTypedArray()

        /**
         * 设置题解示例输入
         * @param initJsonArray 构造函数参数值列表
         * @param parameterTypeNames 构造函数参数类型列表
         */
        public final fun getInit(
            initJsonArray: JsonArray?,
            parameterTypeNames: List<String>,
        ): Array<Any> {
            assertNotNull(
                initJsonArray,
                "The example.init is null",
            )
            assertEquals(
                initJsonArray.size,
                parameterTypeNames.size,
                "The example.init.size != parameter.size $initJsonArray $parameterTypeNames",
            )
            return ExampleSet.json2array(initJsonArray, parameterTypeNames)
        }
    }
}
