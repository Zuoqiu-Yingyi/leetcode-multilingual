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

import { Language } from "./enums";

/**
 * 题目 ID 前缀
 */
export const ID_PREFIX = "s";

/**
 * 题目 ID 长度
 */
export const ID_LENGTH = 4;

/**
 * 题解序号长度
 */
export const SOLUTION_INDEX_LENGTH = 2;

/**
 * 题解临时目录
 */
export const SOLUTIONS_TEMPORARY_DIRECTORY = ".solutions";

/**
 * 题解文件名正则
 */
export const SOLUTION_FILE_NAME_REGEXP = /^s_\d{4}_\d{2}\.(?:js|ts)$/;

/**
 * 题解目录
 */
export const SOLUTIONS_DIRECTORY = {
    [Language.java]: "./solutions/java/lib/src/main/java",
    [Language.kotlin]: "./solutions/kotlin/lib/src/main/kotlin",
    [Language.golang]: "./solutions/go",
    [Language.python3]: "./solutions/python/src",
    [Language.javascript]: "./solutions/ecmascript/src",
    [Language.typescript]: "./solutions/ecmascript/src",
} as const;

export const SOLUTIONS_DIRECTORY_MAP = new Map(Object.entries(SOLUTIONS_DIRECTORY)) as Map<Language, string>;

/**
 * 题解测试目录
 */
export const SOLUTIONS_TEST_DIRECTORY = {
    ...SOLUTIONS_DIRECTORY,
    [Language.java]: "./solutions/java/lib/src/test/java",
    [Language.kotlin]: "./solutions/kotlin/lib/src/test/kotlin",
} as const;

export const SOLUTIONS_TEST_DIRECTORY_MAP = new Map(Object.entries(SOLUTIONS_TEST_DIRECTORY)) as Map<Language, string>;

/**
 * 题解测试用例文件目录
 */
export const SOLUTIONS_TEST_EXAMPLES_DIRECTORY = "./packages/examples";

/**
 * 题解测试用例文件内容
 */
export const SOLUTIONS_TEST_EXAMPLES_CONTENT = `\
[
]
`;

/**
 * 题解函数名称提取正则表达式
 */
export const SOLUTION_FUNCTION_NAME_REGEXP = {
    // public int[] twoSum(int[] nums, int target) {
    [Language.java]: /^\s*public\s+(?<return_type>\S+)\s+(?<function_name>\w+)\s*\((?<arguments>.*)\)\s*\{\s*$/m,
    // fun twoSum(nums: IntArray, target: Int): IntArray {
    [Language.kotlin]: /^\s*fun\s+(?<function_name>\w+)\s*\((?<arguments>.*)\)\s*:\s*(?<return_type>\S+)\s*\{\s*?/m,
    // func twoSum(nums []int, target int) []int {
    [Language.golang]: /^\s*func\s+(?<function_name>\w+)\s*\((?<arguments>.*)\)\s+(?<return_type>\S+)\s*\{\s*$/m,
    // const twoSum = function (nums, target) {
    [Language.javascript]: /^\s*const\s+(?<function_name>\w+)\s*=\s*function\s*\((?<arguments>.*)\)\s*\{\s*$/m,
    // function twoSum(nums: number[], target: number): number[] {
    [Language.typescript]: /^\s*function\s+(?<function_name>\w+)\s*\((?<arguments>.*)\)\s*:\s*(?<return_type>\S+)\s*\{\s*?/m,
} as const;

export const SOLUTION_FUNCTION_NAME_REGEXP_MAP = new Map(Object.entries(SOLUTION_FUNCTION_NAME_REGEXP)) as Map<Language, RegExp>;

/**
 * 题解模板文件名
 */
export const TEMPLATE_FILE_NAME = {
    [Language.java]: "java.mustache",
    [Language.kotlin]: "kotlin.mustache",
    [Language.golang]: "golang.mustache",
    [Language.python3]: "python3.mustache",
    [Language.javascript]: "ecmascript.mustache",
    [Language.typescript]: "ecmascript.mustache",
} as const as Record<Language, string>;

export const TEMPLATE_FILE_NAME_MAP = new Map(Object.entries(TEMPLATE_FILE_NAME)) as Map<Language, string>;
