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
import kotlin.test.*

public class T(
    SolutionsTest: KClass<*>,
    vararg Solutions: KClass<*>,
) {
    val packageName: String
    val Solutions: Array<out KClass<*>>

    lateinit var examples: List<ExampleSet>

    init {
        this.packageName = SolutionsTest
            .qualifiedName
            ?.substringBeforeLast(".")!!
        this.Solutions = Solutions
    }

    companion object {
        /**
         * 通过反射获取题解函数的构造函数
         * @param Solution 题解类
         * @return 题解的入口方法
         */
        final fun getConstructor(Solution: KClass<*>): KCallable<*> {
            // REF: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/
            val constructors = Solution
                .constructors
                .filter { it.visibility == KVisibility.PUBLIC }
            assertEquals(
                constructors.size,
                1,
                "Number of solution constructors is not 1",
            )
            return constructors[0]
        }

        /**
         * 通过反射获取题解的入口方法
         * @param Solution 题解类
         * @return 题解的入口方法
         */
        final fun getMethod(Solution: KClass<*>): KCallable<*> {
            // REF: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/
            val methods = Solution
                .members
                .filter { it.visibility == KVisibility.PUBLIC && !it.isOpen }
            assertEquals(
                methods.size,
                1,
                "Number of solution methods is not 1",
            )
            return methods[0]
        }

        /**
         * 通过反射获取函数的参数个数
         * @param function 函数
         * @return 函数的参数个数
         */
        private final fun getParameterCount(function: KCallable<*>): Int = function.parameters.size

        /**
         * 通过反射获取方法的参数类型名列表
         * @param function 函数
         * @return 函数的参数类型名列表
         */
        private final fun getParameterTypeNames(function: KCallable<*>): List<String> = function.parameters.map { it.type.toString() }

        /**
         * 通过反射获取方法的参数类型名列表
         * 已排除第一个参数 (方法绑定的类)
         * @param method 方法
         * @return 方法的参数类型名列表
         */
        private final fun getMethodParameterTypeNames(method: KCallable<*>): List<String> = method.parameters.drop(1).map { it.type.toString() }

        /**
         * 通过反射获取函数的返回类型名
         * @param function 函数
         * @return 函数的返回类型名
         */
        private final fun getReturnTypeName(function: KCallable<*>): String = function.returnType.toString()

        /**
         * 使用一个示例测试题解
         * @param solution 题解实例
         * @param example 示例
         * @param solutionIndex 题解索引
         * @param examplesIndex 示例集合索引
         * @param exampleIndex 示例索引
         */
        private final fun testExample(
            solution: Any,
            example: Example,
            solutionIndex: Int,
            examplesIndex: Int,
            exampleIndex: Int,
        ) {
            val method = T.getMethod(solution::class)
            val result = method.call(solution, *example.input)
            assertNotNull(result, "Result is null")

            val result_type_name = result::class.qualifiedName
            val output_type_name = example.output::class.qualifiedName
            assertEquals(
                result_type_name,
                output_type_name,
                "Solution result type <$result_type_name> != expected type <$output_type_name>",
            )

            val parameter_type_names = T.getMethodParameterTypeNames(method)
            val return_type_name = T.getReturnTypeName(method)

            val input_json = buildJsonArray {
                example.input.withIndex().forEach { (index, value) ->
                    add(ExampleSet.any2json(value, parameter_type_names[index]))
                }
            }
            val result_json = ExampleSet.any2json(result, return_type_name)
            val output_json = ExampleSet.any2json(example.output, return_type_name)

            assertEquals(
                result_json,
                output_json,
                "\nsolution: $solutionIndex\nexamples: $examplesIndex\nexample:  $exampleIndex\ninput:    $input_json\nresult:   $result_json\nexpected: $output_json\n",
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
            val constructor = T.getConstructor(Solution)
            for ((examplesIndex, examples) in this.examples.withIndex()) {
                val solution = constructor.call(*examples.init)
                assertNotNull(solution, "solution instance is null")
                for ((exampleIndex, example) in examples.examples.withIndex()) {
                    T.testExample(
                        solution,
                        example,
                        solutionIndex,
                        examplesIndex,
                        exampleIndex,
                    )
                }
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
        val constructor = T.getConstructor(this.Solutions[0])
        val constructor_parameter_count = T.getParameterCount(constructor)
        val constructor_parameter_type_names = T.getParameterTypeNames(constructor)

        val method = T.getMethod(this.Solutions[0])
        val method_parameter_count = T.getParameterCount(method)
        val method_parameter_type_names = T.getMethodParameterTypeNames(method)
        val method_return_type_name = T.getReturnTypeName(method)

        for (Solution in this.Solutions) {
            val temp_constructor = T.getConstructor(Solution)
            val temp_constructor_parameter_count = T.getParameterCount(temp_constructor)
            val temp_constructor_parameter_type_names = T.getParameterTypeNames(temp_constructor)

            val temp_method = T.getMethod(Solution)
            val temp_method_parameter_count = T.getParameterCount(temp_method)
            val temp_method_parameter_type_names = T.getMethodParameterTypeNames(temp_method)
            val temp_method_return_type_name = T.getReturnTypeName(temp_method)

            assertEquals(
                temp_constructor_parameter_count,
                constructor_parameter_count,
                "Constructor parameter count is inconsistent",
            )
            assertContentEquals(
                temp_constructor_parameter_type_names,
                constructor_parameter_type_names,
                "Constructor parameter type names is inconsistent",
            )

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

        val examples_json = ExampleSet.readExamplesFile(this.packageName)
        assertNotNull(
            examples_json,
            "No examples",
        )

        this.examples = ExampleSet.fromJson(
            examples_json,
            constructor_parameter_type_names,
            method_parameter_type_names,
            method_return_type_name,
        )
    }
}
