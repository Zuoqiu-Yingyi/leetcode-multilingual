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

import fsAsync from "node:fs/promises";
import path from "node:path";

import Mustache, {
    type OpeningAndClosingTags,
} from "mustache";

import { C, E, U } from "@repo/common";

import type { ISolutionFileContent } from "@repo/common/utils";

export const TEMPLATES_DIRECTORY_NAME = "templates";
export const TEMPLATES_TEST_DIRECTORY_NAME = "test";
export const TEMPLATES_SOLUTION_DIRECTORY_NAME = "solution";

export const TEMPLATE_FILE_NAME = {
    [E.Language.java]: "java.mustache",
    [E.Language.golang]: "golang.mustache",
    [E.Language.javascript]: "ecmascript.mustache",
    [E.Language.typescript]: "ecmascript.mustache",
} as const as Record<E.Language, string>;

export const TEMPLATE_FILE_NAME_MAP = new Map(Object.entries(TEMPLATE_FILE_NAME)) as Map<E.Language, string>;

export const MUSTACHE_TAGS: OpeningAndClosingTags = ["/*{{", "}}*/"] as const;

/**
 * 题解信息
 */
export interface ISolutionInfo {
    language: E.Language;
    id: number;
    index?: number;
}

/**
 * 模板渲染信息
 */
export interface ITemplateRenderInfo {
    id: string; // 0001
    path: string; // _0/_0/_0/_1
    index: string; // 01
    package: string; // _0._0._0._1
}

/**
 * 题解模板渲染信息
 */
export interface ISolutionTemplateRenderInfo extends ITemplateRenderInfo, ISolutionFileContent {
    functionName?: string;
}

/**
 * 文件信息转换为模板渲染信息
 */
export function info2view(info: ISolutionInfo): ITemplateRenderInfo {
    return {
        id: U.idPadZero(info.id),
        path: U.id2paths(info.id, "_").join("/"),
        index: U.idPadZero(info.index ?? 1, C.SOLUTION_INDEX_LENGTH),
        package: U.id2paths(info.id, "_").join("."),
    };
}

/**
 * 渲染题解文件
 * @param info - 题解信息
 * @returns 测试文件内容
 */
export async function renderSolutionFile(
    info: ISolutionInfo,
    content: string,
): Promise<string | void> {
    const template_file_name = TEMPLATE_FILE_NAME_MAP.get(info.language);
    if (template_file_name) {
        const template_file_path = path.join(
            import.meta.dir,
            TEMPLATES_DIRECTORY_NAME,
            TEMPLATES_SOLUTION_DIRECTORY_NAME,
            template_file_name,
        );
        if (await fsAsync.exists(template_file_path)) {
            const template_file = Bun.file(template_file_path);
            const template_content = await template_file.text();

            const solution_content = U.parseSolutionFileParse(content);
            const function_name = U.getSolutionFunctionName(solution_content.code, info.language);

            return `${Mustache.render(
                template_content,
                {
                    ...info2view(info),
                    ...solution_content,
                    functionName: function_name,
                } satisfies ISolutionTemplateRenderInfo,
                undefined,
                {
                    escape: (value) => value,
                    tags: MUSTACHE_TAGS,
                },
            ).trim()}\n`;
        }
    }
}

/**
 * 渲染测试文件
 * @param info - 题解信息
 * @returns 测试文件内容
 */
export async function renderTestFile(info: ISolutionInfo): Promise<string | void> {
    const template_file_name = TEMPLATE_FILE_NAME_MAP.get(info.language);
    if (template_file_name) {
        const template_file_path = path.join(
            import.meta.dir,
            TEMPLATES_DIRECTORY_NAME,
            TEMPLATES_TEST_DIRECTORY_NAME,
            template_file_name,
        );
        if (await fsAsync.exists(template_file_path)) {
            const template_file = Bun.file(template_file_path);
            const template_content = await template_file.text();
            return `${Mustache.render(
                template_content,
                info2view(info),
                undefined,
                {
                    escape: (value) => value,
                    tags: MUSTACHE_TAGS,
                },
            ).trim()}\n`;
        }
    }
}
