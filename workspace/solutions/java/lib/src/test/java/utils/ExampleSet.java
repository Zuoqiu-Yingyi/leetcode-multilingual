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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

/**
 * 示例集合 (相同初始化参数的一组示例)
 */
public class ExampleSet {
    // System.getProperty("user.dir") -> workspace/solutions/java/lib
    public static final String SOLUTIONS_TEST_EXAMPLES_DIRECTORY = "./../../../packages/examples";

    /**
     * 将包名转换为题解 ID
     * @param packageName 包名 (格式: "_0._1._2._3")
     * @return 题解 ID (格式: 0123)
     */
    public static String package2id(final String packageName) {
        return packageName
            .replace("s", "")
            .replace(".", "");
    }

    /**
     * 将包名转换为路径
     * @param packageName 包名 (格式: "_0._1._2._3")
     * @return 路径 (格式: "0/1/2/3")
     */
    public static final String package2path(final String packageName) {
        return packageName
            .replace("s", "")
            .replace(".", "/");
    }

    /**
     * 读取示例文件
     * @param packageName 包名 (格式: "_0._1._2._3")
     * @return JSON 字符串
     */
    public static final String readExamplesFile(final String packageName) {
        try {
            final String id = ExampleSet.package2id(packageName);
            final String path = ExampleSet.package2path(packageName);
            final Path file_path = Paths.get(ExampleSet.SOLUTIONS_TEST_EXAMPLES_DIRECTORY, path, id + ".json");
            final String json = Files.readString(file_path);
            return json;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建示例列表
     * @param json 示例 JSON 字符串
     * @param constructorParameterTypeNames 构造函数参数类型名称列表
     * @param methodParameterTypeNames 方法参数类型名称列表
     * @param methodReturnTypeName 方法返回值类型名称
     * @return 示例列表
     */
    public static final List<ExampleSet> fromJson(
        final String json,
        final String[] constructorParameterTypeNames,
        final String[] methodParameterTypeNames,
        final String methodReturnTypeName
    ) {
        final JSONArray examples_json_array = JSON.parseArray(json);
        final List<ExampleSet> exampleSetList = new ArrayList<ExampleSet>();
        if (constructorParameterTypeNames.length > 0) { // 构造函数有参数
            final List<ExampleWithInit> examplesWithInit = JSON.parseArray(json, ExampleWithInit.class);
            final int examples_count = examplesWithInit.size();
            for (int i = 0; i < examples_count; ++i) {
                final JSONObject example_json = examples_json_array.getJSONObject(i);
                final ExampleWithInit example = examplesWithInit.get(i);

                /* 根据类型解析 JSON 对象 */
                example.setInit(example_json, constructorParameterTypeNames);
                example.setInputs(example_json, methodParameterTypeNames);
                example.setOutputs(example_json, methodReturnTypeName);

                assertEquals(
                    example.init.length,
                    constructorParameterTypeNames.length,
                    "example.init.length != constructor parameters length"
                );
                assertEquals(
                    example.inputs.length,
                    example.outputs.length,
                    "example.inputs.length != example.outputs.length"
                );

                /* 根据 inputs 与 outputs 构造 examples */
                final int examples_length = example.inputs.length;
                final List<Example> examples = new ArrayList<Example>(examples_length);
                for (int j = 0; j < examples_length; j++) {
                    examples.addLast(new Example(example.inputs[j], example.outputs[j]));
                }

                exampleSetList.add(new ExampleSet(
                    example.init,
                    examples
                ));
            }
        }
        else { // 构造函数无参数
            final List<Example> examples = JSON.parseArray(json, Example.class);
            final int examples_count = examples.size();
            for (int i = 0; i < examples_count; ++i) {
                final JSONObject example_json = examples_json_array.getJSONObject(i);
                final Example example = examples.get(i);

                /* 根据类型解析 JSON 对象 */
                example.setInput(example_json, methodParameterTypeNames);
                example.setOutput(example_json, methodReturnTypeName);
            }
            exampleSetList.addLast(new ExampleSet(new Object[0], examples));
        }
        return exampleSetList;
    }

    /**
     * 解析 JSON 列表
     * @param jsonArray JSON 列表
     * @param typeNames 类型名称列表
     */
    public static Object[] jsonList(
        final JSONArray jsonArray,
        final String[] typeNames
    ) {
        final int array_length = jsonArray.size();
        assertEquals(
            array_length,
            typeNames.length,
            "Number of JSON Array != Number of Types"
        );

        final Object[] array = new Object[jsonArray.size()];
        for (int i = 0; i < array_length; ++i) {
            switch (typeNames[i]) {
                case "String":
                    array[i] = jsonArray.getString(i);
                    break;
                case "int":
                    array[i] = jsonArray.getIntValue(i);
                    break;
                case "int[]":
                    array[i] =
                        jsonArray
                            .getJSONArray(i)
                            .toJavaList(Integer.class)
                            .stream()
                            .mapToInt(Integer::intValue)
                            .toArray();
                    break;

                default:
                    array[i] = jsonArray.get(i);
                    break;
            }
        }
        return array;
    }

    /**
     * 解析 JSON 值
     * @param jsonObject JSON 对象
     * @param valueKey 字段名称
     * @param typeName 类型名称
     */
    public static Object jsonValue(
        final JSONObject jsonObject,
        final String valueKey,
        final String typeName
    ) {
        switch (typeName) {
            case "String":
                return jsonObject.getString(valueKey);
            case "int":
                return jsonObject.getIntValue(valueKey);
            case "int[]":
                return jsonObject
                    .getJSONArray(valueKey)
                    .toJavaList(Integer.class)
                    .stream()
                    .mapToInt(Integer::intValue)
                    .toArray();
            default:
                return jsonObject.get(valueKey);
        }
    }

    /**
     * 题解构造函数参数
     */
    public Object[] init;

    /**
     * 题解输入输出示例列表
     */
    public List<Example> examples;

    public ExampleSet(
        final Object[] init,
        final List<Example> examples
    ) {
        this.init = init;
        this.examples = examples;
    }
}
