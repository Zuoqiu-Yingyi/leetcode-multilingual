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
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

/**
 * 题解测试示例
 */
public class Example {
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
            final String id = Example.package2id(packageName);
            final String path = Example.package2path(packageName);
            final Path file_path = Paths.get(Example.SOLUTIONS_TEST_EXAMPLES_DIRECTORY, path, id + ".json");
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
     * @param methodParameterTypeNames 方法参数类型名称列表
     * @param methodReturnTypeName 方法返回值类型名称
     * @return 示例列表
     */
    public static final List<Example> fromJson(
        final String json,
        final String[] methodParameterTypeNames,
        final String methodReturnTypeName
    ) {
        final JSONArray examples_json_array = JSON.parseArray(json);
        final List<Example> examples = JSON.parseArray(json, Example.class);

        final int examples_count = examples.size();
        for (int i = 0; i < examples_count; ++i) {
            final JSONObject example_json = examples_json_array.getJSONObject(i);
            final Example example = examples.get(i);
            example.setInput(example_json, methodParameterTypeNames);
            example.setOutput(example_json, methodReturnTypeName);
        }
        return examples;
    }

    /**
     * 题解示例输入
     */
    public Object[] input = new Object[0];

    /**
     * 题解示例输出
     */
    public Object output = null;

    /**
     * 设置题解示例输入
     * @param exampleJsonObject 示例 JSON 对象
     * @param parameterTypeNames 参数类型名称列表
     */
    public void setInput(
        final JSONObject exampleJsonObject,
        final String[] parameterTypeNames
    ) {
        final String key = "input";
        final JSONArray inputJSONArray = exampleJsonObject.getJSONArray(key);
        assertEquals(
            inputJSONArray.size(),
            parameterTypeNames.length,
            "Number of input != Number of parameter"
        );
        final int input_len = inputJSONArray.size();
        final Object[] input = new Object[inputJSONArray.size()];
        for (int i = 0; i < input_len; ++i) {
            switch (parameterTypeNames[i]) {
                case "String":
                    input[i] = inputJSONArray.getString(i);
                    break;
                case "int":
                    input[i] = inputJSONArray.getIntValue(i);
                    break;
                case "int[]":
                    input[i] =
                        inputJSONArray
                            .getJSONArray(i)
                            .toJavaList(Integer.class)
                            .stream()
                            .mapToInt(Integer::intValue)
                            .toArray();
                    break;

                default:
                    input[i] = inputJSONArray.get(i);
                    break;
            }
        }
        this.input = input;
    }

    /**
     * 设置题解示例输出
     * @param exampleJsonObject 示例 JSON 对象
     * @param returnTypeName 返回值类型名称
     */
    public void setOutput(
        final JSONObject exampleJsonObject,
        final String returnTypeName
    ) {
        final String key = "output";
        switch (returnTypeName) {
            case "String":
                this.output = exampleJsonObject.getString(key);
                break;
            case "int":
                this.output = exampleJsonObject.getIntValue(key);
                break;
            case "int[]":
                this.output =
                    exampleJsonObject
                        .getJSONArray(key)
                        .toJavaList(Integer.class)
                        .stream()
                        .mapToInt(Integer::intValue)
                        .toArray();
                break;

            default:
                this.output = exampleJsonObject.get(key);
                break;
        }
    }
}
