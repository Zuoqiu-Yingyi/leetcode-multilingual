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

import com.alibaba.fastjson2.JSONObject;

/**
 * 题解测试示例
 */
public class Example {
    /**
     * 题解示例输入
     */
    public Object[] input;

    /**
     * 题解示例输出
     */
    public Object output;

    public Example(
        Object[] input,
        Object output
    ) {
        this.input = input;
        this.output = output;
    }

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
        this.input = ExampleSet.jsonList(
            exampleJsonObject.getJSONArray(key),
            parameterTypeNames
        );
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
        this.output = ExampleSet.jsonValue(
            exampleJsonObject,
            key,
            returnTypeName
        );
    }
}
