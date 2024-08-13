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
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.test.*

public class Test_(
    SolutionsTest: KClass<*>,
    vararg Solutions: KClass<*>,
) {
    val packageName: String
    val Solutions: Array<out KClass<*>>

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
        private final fun getMethod(Solution: KClass<*>): KCallable<*> {
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
         * @param method 方法
         * @return 方法的参数类型名列表
         */
        private final fun getMethodParameterTypeNames(method: KCallable<*>): List<String> = method.parameters.map { it.type.toString() }

        /**
         * 通过反射获取方法的返回类型名
         * @param method 方法
         * @return 方法的返回类型名
         */
        private final fun getMethodReturnTypeName(method: KCallable<*>): String = method.returnType.toString()
    }

    public final fun run() {
        this.test()
    }

    /**
     * 测试题解
     */
    private final fun test() {
        this.testSolutionsTypeDefinition()

        // TODO: 运行题解
        // TODO: 校验题解结果
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

        // TODO: 加载题解测试示例
        // TODO: 构造题解测试示例对象
        assertTrue(false, "$method_return_type_name")
    }
}
