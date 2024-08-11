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
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.lang.reflect.Method;
import java.util.List;

public class Test_ {
    // System.getProperty("user.dir") -> workspace/solutions/java/lib

    /**
     * 通过反射获取题解的入口方法
     * @param Solution 题解类
     * @return 题解的入口方法
     */
    private static final Method getMethod(final Class<?> Solution)
    {
        Method[] methods = Solution.getDeclaredMethods();
        assertEquals(methods.length, 1, "Number of solution methods is not 1");
        return methods[0];
    }

    /**
     * 通过反射获取题解的包名, 可用于获取题解的 ID
     * @param Solution 题解类
     * @return 题解的包名
     */
    private static final String getPackageName(final Class<?> Solution)
    {
        return Solution.getPackageName();
    }

    /**
     * 通过反射获取方法的参数个数
     * @param method 方法
     * @return 方法的参数个数
     */
    private static final int getMethodParameterCount(final Method method)
    {
        return method.getParameterCount();
    }

    /**
     * 通过反射获取方法的参数类型列表
     * @param method 方法
     * @return 方法的参数类型列表
     */
    private static final Class<?>[] getMethodParameterTypes(final Method method)
    {
        Class<?>[] parameter_types = method.getParameterTypes();
        return parameter_types;
    }

    /**
     * 通过反射获取方法的返回值类型
     * @param method 方法
     * @return 方法的返回值类型
     */
    private static final Class<?> getMethodReturnType(final Method method)
    {
        Class<?> return_type = method.getReturnType();
        return return_type;
    }

    /**
     * 通过反射获取方法的参数类型名称列表
     * @param method 方法
     * @return 方法的参数类型名称列表
     */
    private static final String[] getMethodParameterTypeNames(final Method method)
    {
        Class<?>[] parameter_types = Test_.getMethodParameterTypes(method);
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
    private static final String getMethodReturnTypeName(final Method method)
    {
        Class<?> return_type = Test_.getMethodReturnType(method);
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
    private static final void testExample(final Object solution, final Example example, final int solutionIndex, final int exampleIndex) throws Exception
    {
        // 题解入口方法
        Method[] methods = solution.getClass().getDeclaredMethods();
        assertEquals(methods.length, 1, "Number of solution methods is not 1");
        Method method = methods[0];

        final Object result = method.invoke(solution, example.input);

        final String result_type_name = result.getClass().getTypeName();
        final String output_type_name = example.output.getClass().getTypeName();

        assertEquals(result_type_name, output_type_name, String.format("Solution result type <%s> != expected type <%s>", result_type_name, output_type_name));

        final String result_json = JSON.toJSONString(result);
        final String output_json = JSON.toJSONString(example.output);

        final String message = String.format("\nsolution: %d\nexample:  %d\ninput:    %s\nresult:   %s\nexpected: %s\n", solutionIndex, exampleIndex, JSON.toJSONString(example.input), result_json, output_json);

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

    private final Class<?>[] Solutions;
    private List<Example> examples;

    /**
     * 构造函数
     * @param Solutions 题解类列表
     */
    public Test_(final Class<?>... Solutions)
    {
        this.Solutions = Solutions;
    }

    /**
     * 运行测试
     */
    public void run()
    {
        assertDoesNotThrow(() -> this.test());
    }

    /**
     * 测试题解
     * @throws Exception
     */
    private void test() throws Exception
    {
        this.testSolutionsTypeDefinition();

        int solution_index = 0;
        for (Class<?> Solution : this.Solutions) {
            final Object solution = Solution.getDeclaredConstructor().newInstance();
            int example_index = 0;
            for (Example example : this.examples) {
                final int _solution_index = solution_index;
                final int _example_index = example_index;
                assertDoesNotThrow(() -> {
                    Test_.testExample(solution, example, _solution_index, _example_index);
                });
                example_index++;
            }
            solution_index++;
        }
    }

    /**
     * 测试题解类型定义是否符合要求
     */
    private void testSolutionsTypeDefinition()
    {
        assertTrue(this.Solutions.length > 0, "No solutions");

        final Method method = Test_.getMethod(this.Solutions[0]);
        final String package_name = Test_.getPackageName(this.Solutions[0]);
        final int method_parameter_count = Test_.getMethodParameterCount(method);
        final String[] method_parameter_type_names = Test_.getMethodParameterTypeNames(method);
        final String method_return_type_name = Test_.getMethodReturnTypeName(method);
        for (Class<?> Solution : this.Solutions) {
            final Method _method = Test_.getMethod(Solution);
            final String _package_name = Test_.getPackageName(Solution);
            final int _method_parameter_count = Test_.getMethodParameterCount(_method);
            final String[] _method_parameter_type_names = Test_.getMethodParameterTypeNames(_method);
            final String _method_return_type_name = Test_.getMethodReturnTypeName(_method);

            assertEquals(_package_name, package_name, "Package name is inconsistent");
            assertEquals(_method_parameter_count, method_parameter_count, "Method parameter count is inconsistent");
            assertArrayEquals(_method_parameter_type_names, method_parameter_type_names, "Method parameter type names is inconsistent");
            assertEquals(
                _method_return_type_name, method_return_type_name, "Method return type name is inconsistent"
            );
        }

        final String examples_json = Example.readExamplesFile(package_name);
        final JSONArray examples_json_array = JSON.parseArray(examples_json);
        this.examples = Example.getExamples(examples_json);

        final int examples_count = examples.size();
        for (int i = 0; i < examples_count; ++i) {
            final JSONObject example_json = examples_json_array.getJSONObject(i);
            final Example example = examples.get(i);
            example.setInput(example_json, method_parameter_type_names);
            example.setOutput(example_json, method_return_type_name);
        }
    }
}
