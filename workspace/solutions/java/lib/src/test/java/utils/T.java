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

package utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.List;

import com.alibaba.fastjson2.JSON;

public class T {
    // System.getProperty("user.dir") -> workspace/solutions/java/lib

    /**
     * 通过反射获取题解的入口方法
     * @param Solution 题解类
     * @return 题解的入口方法
     */
    private static final Method getMethod(final Class<?> Solution) {
        Method[] methods = Solution.getDeclaredMethods();
        assertEquals(
            methods.length,
            1,
            "Number of solution methods is not 1"
        );
        return methods[0];
    }

    /**
     * 通过反射获取方法的参数个数
     * @param method 方法
     * @return 方法的参数个数
     */
    private static final int getMethodParameterCount(final Method method) {
        return method.getParameterCount();
    }

    /**
     * 通过反射获取方法的参数类型列表
     * @param method 方法
     * @return 方法的参数类型列表
     */
    private static final Class<?>[] getMethodParameterTypes(final Method method) {
        Class<?>[] parameter_types = method.getParameterTypes();
        return parameter_types;
    }

    /**
     * 通过反射获取方法的返回值类型
     * @param method 方法
     * @return 方法的返回值类型
     */
    private static final Class<?> getMethodReturnType(final Method method) {
        Class<?> return_type = method.getReturnType();
        return return_type;
    }

    /**
     * 通过反射获取方法的参数类型名称列表
     * @param method 方法
     * @return 方法的参数类型名称列表
     */
    private static final String[] getMethodParameterTypeNames(final Method method) {
        Class<?>[] parameter_types = T.getMethodParameterTypes(method);
        final String[] parameter_type_names = new String[parameter_types.length];
        for (int i = 0; i < parameter_types.length; ++i) {
            // getName: [I java.lang.String
            // getTypeName: int[] java.lang.String
            // getSimpleName: int[] String
            // getCanonicalName: int[] java.lang.String
            parameter_type_names[i] = parameter_types[i].getSimpleName();
        }
        return parameter_type_names;
    }

    /**
     * 通过反射获取方法的返回值类型名称
     * @param method 方法
     * @return 方法的返回值类型名称
     */
    private static final String getMethodReturnTypeName(final Method method) {
        Class<?> return_type = T.getMethodReturnType(method);
        final String return_type_name = return_type.getSimpleName();
        return return_type_name;
    }

    /**
     * 使用一个示例测试题解
     * @param solution 题解实例
     * @param example 示例
     * @param solutionIndex 题解索引
     * @param exampleIndex 示例索引
     * @throws Exception
     */
    private static final void testExample(
        final Object solution,
        final Example example,
        final int solutionIndex,
        final int exampleIndex
    ) throws Exception {
        // 题解入口方法
        Method method = T.getMethod(solution.getClass());

        final Object result = method.invoke(solution, example.input);

        final String result_type_name = result.getClass().getTypeName();
        final String output_type_name = example.output.getClass().getTypeName();

        assertEquals(
            result_type_name,
            output_type_name,
            String.format(
                "Solution result type <%s> != expected type <%s>",
                result_type_name,
                output_type_name
            )
        );

        final String input_json = JSON.toJSONString(example.input);
        final String result_json = JSON.toJSONString(result);
        final String output_json = JSON.toJSONString(example.output);

        final String message = String.format(
            "\nsolution: %d\nexample:  %d\ninput:    %s\nresult:   %s\nexpected: %s\n",
            solutionIndex,
            exampleIndex,
            input_json,
            result_json,
            output_json
        );

        assertEquals(result_json, output_json, message);

        // switch (result_type_name) {
        // case "int[]":
        //     assertArrayEquals((int[])result, (int[])example.output, message);
        //     break;
        // default:
        //     assertEquals(result, example.output, message);
        //     break;
        // }
    }

    private final String packageName;
    private final Class<?>[] Solutions;
    private List<Example> examples;

    /**
     * 构造函数
     * @param packageName 测试类的包名
     * @param Solutions 题解类列表
     */
    public T(
        final Class<?> SolutionTest,
        final Class<?>... Solutions
    ) {
        this.packageName = SolutionTest.getPackageName();
        this.Solutions = Solutions;
    }

    /**
     * 运行测试
     */
    public void run() {
        assertDoesNotThrow(() -> this.test());
    }

    /**
     * 测试题解
     * @throws Exception
     */
    private void test() throws Exception {
        this.testSolutionsTypeDefinition();

        int solution_index = 0;
        for (Class<?> Solution : this.Solutions) {
            final Object solution = Solution.getDeclaredConstructor().newInstance();
            int example_index = 0;
            for (Example example : this.examples) {
                final int _solution_index = solution_index;
                final int _example_index = example_index;
                assertDoesNotThrow(() -> {
                    T.testExample(
                        solution,
                        example,
                        _solution_index,
                        _example_index
                    );
                });
                example_index++;
            }
            solution_index++;
        }
    }

    /**
     * 测试题解类型定义是否符合要求
     */
    private void testSolutionsTypeDefinition() {
        assertTrue(this.Solutions.length > 0, "No solutions");

        final Method method = T.getMethod(this.Solutions[0]);
        final int method_parameter_count = T.getMethodParameterCount(method);
        final String[] method_parameter_type_names = T.getMethodParameterTypeNames(method);
        final String method_return_type_name = T.getMethodReturnTypeName(method);
        for (Class<?> Solution : this.Solutions) {
            final Method _method = T.getMethod(Solution);
            final int _method_parameter_count = T.getMethodParameterCount(_method);
            final String[] _method_parameter_type_names = T.getMethodParameterTypeNames(_method);
            final String _method_return_type_name = T.getMethodReturnTypeName(_method);

            assertEquals(
                _method_parameter_count,
                method_parameter_count,
                "Method parameter count is inconsistent"
            );
            assertArrayEquals(
                _method_parameter_type_names,
                method_parameter_type_names,
                "Method parameter type names is inconsistent"
            );
            assertEquals(
                _method_return_type_name,
                method_return_type_name,
                "Method return type name is inconsistent"
            );
        }

        final String examples_json = Example.readExamplesFile(this.packageName);
        assertNotEquals(
            examples_json,
            null,
            "Examples file not found"
        );

        this.examples = Example.fromJson(
            examples_json,
            method_parameter_type_names,
            method_return_type_name
        );
    }
}
