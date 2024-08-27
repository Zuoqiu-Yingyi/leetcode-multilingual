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

import java.util.Arrays;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

/**
 * 含有构造函数初始化列表的题解测试示例
 */
public class ExampleWithInit {
    /**
     * 题解示例输入
     */
    public Object[] init;

    /**
     * 题解示例输入
     */
    public Object[][] inputs;

    /**
     * 题解示例输出
     */
    public Object[] outputs;

    /**
     * 设置题解构造函数参数列表
     * @param exampleJsonObject 示例 JSON 对象
     * @param parameterTypeNames 参数类型名称列表
     */
    public void setInit(
        final JSONObject exampleJsonObject,
        final String[] parameterTypeNames
    ) {
        final String key = "init";
        final JSONArray inputJSONArray = exampleJsonObject.getJSONArray(key);
        this.init = ExampleSet.jsonList(inputJSONArray, parameterTypeNames);
    }

    /**
     * 设置题解示例输入
     * @param exampleJsonObject 示例 JSON 对象
     * @param parameterTypeNames 参数类型名称列表
     */
    public void setInputs(
        final JSONObject exampleJsonObject,
        final String[] parameterTypeNames
    ) {
        final String key = "inputs";
        final JSONArray inputsJsonArray = exampleJsonObject.getJSONArray(key);
        for (int i = 0; i < inputsJsonArray.size(); i++) {
            this.inputs[i] = ExampleSet.jsonList(
                inputsJsonArray.getJSONArray(i),
                parameterTypeNames
            );
        }
    }

    /**
     * 设置题解示例输出
     * @param exampleJsonObject 示例 JSON 对象
     * @param returnTypeName 返回值类型名称
     */
    public void setOutputs(
        final JSONObject exampleJsonObject,
        final String returnTypeName
    ) {
        final String key = "outputs";
        final JSONArray outputsJsonArray = exampleJsonObject.getJSONArray(key);
        final String[] returnTypeNames = new String[outputsJsonArray.size()];
        Arrays.fill(returnTypeNames, returnTypeName);
        this.outputs = ExampleSet.jsonList(
            outputsJsonArray,
            returnTypeNames
        );
    }
}
