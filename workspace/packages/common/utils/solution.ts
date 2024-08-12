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

import { C } from "./..";

import type { E } from "./..";

/**
 * 题解文件内容
 */
export interface ISolutionFileContent {
    head: string;
    code: string;
    tail: string;
}

/**
 * 解析题解文件内容
 * @param content - 文件内容
 * @returns 题解文件内容
 */
export function parseSolutionFileParse(
    content: string,
): ISolutionFileContent {
    const lines = content.split(/\r?\n/);
    const code_start = lines.findIndex((line) => line.trimEnd().endsWith("@lc code=start"));
    const code_end = lines.findLastIndex((line) => line.trimEnd().endsWith("@lc code=end"));
    if (code_start > 0
        && code_end > code_start
        && lines.length > code_end
    ) {
        return {
            head: lines.slice(0, code_start).join("\n").trim(),
            code: lines.slice(code_start, code_end + 1).join("\n").trim(),
            tail: lines.slice(code_end + 1).join("\n").trim(),
        } satisfies ISolutionFileContent;
    }
    throw new Error("Invalid solution file");
}

/**
 * 获取题解函数名称
 * @param code - 代码文本
 * @param language - 代码语言
 * @returns 函数名称
 */
export function getSolutionFunctionName(
    code: string,
    language: E.Language,
): string | undefined {
    return C.SOLUTION_FUNCTION_NAME_REGEXP_MAP.get(language)
        ?.exec(code)
        ?.groups
        ?.function_name;
}
