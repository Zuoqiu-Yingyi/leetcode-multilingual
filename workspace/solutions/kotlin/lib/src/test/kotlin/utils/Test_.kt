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

// REF: https://kotlinlang.org/api/latest/kotlin.test/kotlin.test/
import kotlinx.serialization.json.buildJsonArray
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createInstance
import kotlin.test.*

public class Test_(
    SolutionsTest: KClass<*>,
    vararg Solutions: KClass<*>,
) {
    val packageName: String
    val Solutions: Array<out KClass<*>>

    lateinit var examples: List<Example>

    init {
        this.packageName = SolutionsTest
            .qualifiedName
            ?.substringBeforeLast(".")!!
        this.Solutions = Solutions
    }

    companion object {
        /**
         * 通过反射获取题解的入口方法
         * @param Solution 题解类
         * @return 题解的入口方法
         */
        final fun getMethod(Solution: KClass<*>): KCallable<*> {
            // REF: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/
            val methods = Solution
                .members
                .filter {
                    it.visibility == KVisibility.PUBLIC &&
                        !it.isOpen
                }
            assertEquals(
                methods.size,
                1,
                "Number of solution methods is not 1",
            )
            return methods[0]
        }

        /**
         * 通过反射获取方法的参数个数
         * @param method 方法
         * @return 方法的参数个数
         */
        private final fun getMethodParameterCount(method: KCallable<*>): Int = method.parameters.size

        /**
         * 通过反射获取方法的参数类型名列表
         * 已排除第一个参数 (方法绑定的类)
         * @param method 方法
         * @return 方法的参数类型名列表
         */
        private final fun getMethodParameterTypeNames(method: KCallable<*>): List<String> = method.parameters.drop(1).map { it.type.toString() }

        /**
         * 通过反射获取方法的返回类型名
         * @param method 方法
         * @return 方法的返回类型名
         */
        private final fun getMethodReturnTypeName(method: KCallable<*>): String = method.returnType.toString()

        /**
         * 使用一个示例测试题解
         * @param solution 题解实例
         * @param example 示例
         * @param solutionIndex 题解索引
         * @param exampleIndex 示例索引
         */
        private final fun testExample(
            solution: Any,
            example: Example,
            solutionIndex: Int,
            exampleIndex: Int,
        ) {
            val method = Test_.getMethod(solution::class)
            val result = method.call(solution, *example.input)
            assertNotNull(result, "Result is null")

            val result_type_name = result::class.qualifiedName
            val output_type_name = example.output::class.qualifiedName
            assertEquals(
                result_type_name,
                output_type_name,
                "Solution result type <$result_type_name> != expected type <$output_type_name>",
            )

            val parameter_type_names = Test_.getMethodParameterTypeNames(method)
            val return_type_name = Test_.getMethodReturnTypeName(method)

            val input_json = buildJsonArray {
                example.input.withIndex().forEach { (index, value) ->
                    add(Example.any2json(value, parameter_type_names[index]))
                }
            }
            val result_json = Example.any2json(result, return_type_name)
            val output_json = Example.any2json(example.output, return_type_name)

            assertEquals(
                result_json,
                output_json,
                "\nsolution: $solutionIndex\nexample:  $exampleIndex\ninput:    $input_json\nresult:   $result_json\nexpected: $output_json\n",
            )
        }
    }

    public final fun run() {
        this.test()
    }

    /**
     * 测试题解
     */
    private final fun test() {
        this.testSolutionsTypeDefinition()

        for ((solutionIndex, Solution) in this.Solutions.withIndex()) {
            val solution = Solution.createInstance()
            for ((exampleIndex, example) in this.examples.withIndex()) {
                Test_.testExample(
                    solution,
                    example,
                    solutionIndex,
                    exampleIndex,
                )
            }
        }
    }

    /**
     * 测试题解类型定义是否符合要求
     */
    private final fun testSolutionsTypeDefinition() {
        assertTrue(
            this.Solutions.size > 0,
            "No solutions",
        )

        // REF: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-callable/
        val method = Test_.getMethod(this.Solutions[0])
        val method_parameter_count = Test_.getMethodParameterCount(method)
        val method_parameter_type_names = Test_.getMethodParameterTypeNames(method)
        val method_return_type_name = Test_.getMethodReturnTypeName(method)

        for (Solution in this.Solutions) {
            val temp_method = Test_.getMethod(Solution)
            val temp_method_parameter_count = Test_.getMethodParameterCount(temp_method)
            val temp_method_parameter_type_names = Test_.getMethodParameterTypeNames(temp_method)
            val temp_method_return_type_name = Test_.getMethodReturnTypeName(temp_method)

            assertEquals(
                temp_method_parameter_count,
                method_parameter_count,
                "Method parameter count is inconsistent",
            )
            assertContentEquals(
                temp_method_parameter_type_names,
                method_parameter_type_names,
                "Method parameter type names is inconsistent",
            )
            assertEquals(
                temp_method_return_type_name,
                method_return_type_name,
                "Method return type name is inconsistent",
            )
        }

        val examples_json = Example.readExamplesFile(this.packageName)
        assertNotNull(
            examples_json,
            "No examples",
        )

        this.examples = Example.fromJson(
            examples_json,
            method_parameter_type_names,
            method_return_type_name,
        )
    }
}
