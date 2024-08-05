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
 * ID 长度
 */
export const ID_LENGTH = 4;

/**
 * 题解临时目录
 */
export const SOLUTIONS_TEMPORARY_DIRECTORY = ".solutions";

/**
 * 题解目录
 */
export const SOLUTIONS_DIRECTORY = {
    [Language.golang]: "./solutions/go",
    [Language.javascript]: "./solutions/ecmascript/src",
    [Language.typescript]: "./solutions/ecmascript/src",
} as const;

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
 * 题解测试文件名
 */
export const ES_SOLUTIONS_TEST_FILE_NAME = "index.test.ts";

export const ES_SOLUTIONS_TEST_FILE_CONTENT = `\
import test from "bun:test";

import { t } from "@/utils/test";

t(
    test,
    import.meta.dir,
);
`;
