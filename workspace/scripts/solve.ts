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
import process from "node:process";

import chalk from "chalk";
import chokidar from "chokidar";

import { C, E } from "@repo/common";

import type fs from "node:fs";

console.log(import.meta.url);

/**
 * 题解信息
 */
interface ISolutionInfo {
    id: number;
    difficulty: E.Difficulty;
    name: string;
    language: E.Language;
    ext: E.Extension;
}

/**
 * 解析题解文件路径
 * @param filePath - 文件相对路径
 * @returns 题解信息
 */
function parseSolutionFilePath(filePath: string): ISolutionInfo | void {
    const { dir, name: language, ext } = path.parse(filePath);
    const [directory, problem] = dir.split(path.sep);
    if (directory === C.SOLUTIONS_TEMPORARY_DIRECTORY && problem) {
        const [id, difficulty, name] = problem.split(".");
        if (id && difficulty && name) {
            return {
                id: Number.parseInt(id),
                difficulty: difficulty as E.Difficulty,
                name,
                language: language as E.Language,
                ext: ext as E.Extension,
            } satisfies ISolutionInfo;
        }
    }
}

/**
 * 题目 ID 填充前导零
 * @param id - 题目 ID
 * @param maxLength - 填充后的最大长度
 * @returns 填充后的题目 ID
 */
function idPadZero(
    id: number,
    maxLength = C.ID_LENGTH,
): string {
    return String(id).padStart(maxLength, "0");
}

/**
 * 题目 ID 转换为目录路径
 * @param id - 题目 ID
 * @param prefix - 目录名前缀
 * @param suffix - 目录名后缀
 * @returns 目录路径列表
 */
function id2paths(
    id: number,
    prefix = "",
    suffix = "",
): string[] {
    return idPadZero(id).split("").map((digit) => `${prefix}${digit}${suffix}`);
}

/**
 * 构造题解目录路径
 * @param language - 题解语言
 * @param id - 题解编号
 * @returns 目录路径
 */
function solutionDirectory(
    language: E.Language,
    id: number,
): string {
    // @ts-expect-error info is not assignable to type 'never'
    const source_root_directory: string | undefined = C.SOLUTIONS_DIRECTORY[language];
    if (source_root_directory) {
        const paths = id2paths(id, "_");
        switch (language) {
            case E.Language.java:
            case E.Language.golang:
            case E.Language.javascript:
            case E.Language.typescript:
            default:
                return path.join(source_root_directory, ...paths);
        }
    }
    throw new Error(`Unsupported language: ${language}`);
}

/**
 * 构造题解测试目录路径
 * @param language - 题解语言
 * @param id - 题解编号
 * @returns 目录路径
 */
function solutionTestDirectory(
    language: E.Language,
    id: number,
): string {
    // @ts-expect-error info is not assignable to type 'never'
    const source_root_directory: string | undefined = C.SOLUTIONS_TEST_DIRECTORY[language];
    if (source_root_directory) {
        const paths = id2paths(id, "_");
        switch (language) {
            case E.Language.java:
            case E.Language.golang:
            case E.Language.javascript:
            case E.Language.typescript:
            default:
                return path.join(source_root_directory, ...paths);
        }
    }
    throw new Error(`Unsupported language: ${language}`);
}

/**
 * 构造题解文件名
 * @param info - 题解信息
 * @param index - 题解序号
 * @returns 文件名
 */
function solutionFileName(
    info: ISolutionInfo,
    index: number,
): string {
    const name = `${String(info.id).padStart(C.ID_LENGTH, "0")}_${String(index).padStart(2, "0")}`;
    switch (info.language) {
        case E.Language.golang:
            return `s_${name}/s_${name}${info.ext}`;
        case E.Language.java:
        case E.Language.javascript:
        case E.Language.typescript:
        default:
            return `s_${name}${info.ext}`;
    }
}

/**
 * 创建题解文件
 * @param info - 题解信息
 * @param original - 原文件路径
 * @returns 新文件路径
 */
async function createSolutionFile(
    info: ISolutionInfo,
    original: string,
): Promise<string | void> {
    try {
        const destination_directory_path = solutionDirectory(info.language, info.id);

        for (let index = 1; true; index++) {
            const destination = path.join(destination_directory_path, solutionFileName(info, index));
            if (await fsAsync.exists(destination)) {
                continue;
            }
            else {
                await Bun.sleep(1_000); // 避免 VSCode 扩展 LeetCode.vscode-leetcode 重复创建文件

                /* 在指定位置创建题解模板文件 */
                const content = await Bun.file(original).text();
                await Bun.write(
                    destination,
                    content.replaceAll("\r\n", "\n"),
                    { createPath: true },
                );

                /* 删除 VSCode 插件 LeetCode.vscode-leetcode 创建的题解模板文件 */
                await fsAsync.unlink(original);
                return destination;
            }
        }
    }
    catch (error) {
        console.warn(error);
    }
}

/**
 * 创建题解测试文件
 * @param info - 题解信息
 * @returns 测试文件路径
 */
async function createSolutionTestFile(
    info: ISolutionInfo,
): Promise<string | void> {
    try {
        const destination_directory_path = solutionTestDirectory(info.language, info.id);
        switch (info.language) {
            case E.Language.golang:
            case E.Language.javascript:
            case E.Language.typescript: {
                /* 覆写测试文件以触发 bun 的测试 */
                await Bun.write(
                    path.join(destination_directory_path, `s_${idPadZero(info.id)}.test.ts`),
                    C.ES_SOLUTIONS_TEST_FILE_CONTENT,
                    { createPath: true },
                );
                break;
            }
            default:
                break;
        }
    }
    catch (error) {
        console.warn(error);
    }
}

/**
 * 创建题解测试用例文件
 * @param info - 题解信息
 * @returns 测试用例文件路径
 */
async function createSolutionTestExamplesFile(info: ISolutionInfo) {
    const id = idPadZero(info.id);
    const paths = id2paths(info.id);
    const examples_file_path = path.join(process.cwd(), C.SOLUTIONS_TEST_EXAMPLES_DIRECTORY, ...paths, `${id}.json`);
    if (!(await fsAsync.exists(examples_file_path))) {
        await Bun.write(
            examples_file_path,
            C.SOLUTIONS_TEST_EXAMPLES_CONTENT,
            { createPath: true },
        );
    }
    return examples_file_path;
}

/**
 * 事件名称文本宽度
 */
const EVENT_NAME_WIDTH = 10;

type TEntryEventName = "add" | "addDir" | "change" | "unlink" | "unlinkDir";

/**
 * @param eventName - 事件名称
 * @param entryPath - 资源路径
 */
function printEntryEvent(
    eventName: TEntryEventName,
    entryPath: string,
) {
    let event_name: string = eventName;
    const blank_spaces = " ".repeat(EVENT_NAME_WIDTH - eventName.length);
    const entry_path = path.join(process.cwd(), entryPath);
    switch (eventName) {
        case "add":
        case "addDir":
            event_name = chalk.green(event_name);
            break;
        case "change":
            event_name = chalk.cyan(event_name);
            break;
        case "unlink":
        case "unlinkDir":
            event_name = chalk.red(event_name);
            break;
        default:
            break;
    }
    console.debug([
        event_name,
        blank_spaces,
        entry_path,
    ].join(""));
}

/**
 * 处理文件资源变化
 * @param eventName - 事件名称
 * @param entryPath - 资源路径
 * @param _stats - 文件状态
 */
async function solutionsHandler(
    eventName: TEntryEventName,
    entryPath: string,
    _stats?: fs.Stats,
) {
    printEntryEvent(eventName, entryPath);
    let examples_file_path: string | void = void null;
    let solution_file_path: string | void = void null;
    let solution_test_file_path: string | void = void null;
    switch (eventName) {
        case "add": {
            const file_info = parseSolutionFilePath(entryPath);
            if (file_info) {
                // console.log(file_info);
                switch (file_info.language) {
                    case E.Language.golang:
                    case E.Language.javascript:
                    case E.Language.typescript: {
                        examples_file_path = await createSolutionTestExamplesFile(file_info);
                        solution_file_path = await createSolutionFile(file_info, entryPath);
                        solution_test_file_path = await createSolutionTestFile(file_info);
                        break;
                    }
                }
            }
            break;
        }
        default:
            break;
    }
    if (examples_file_path) {
        console.debug([
            "🌰  ".padStart(EVENT_NAME_WIDTH),
            chalk.green(examples_file_path),
        ].join(""));
    }
    if (solution_file_path) {
        console.debug([
            "➜  ".padStart(EVENT_NAME_WIDTH),
            chalk.green(path.join(process.cwd(), solution_file_path)),
        ].join(""));
    }
    if (solution_test_file_path) {
        console.debug([
            "🧪  ".padStart(EVENT_NAME_WIDTH),
            chalk.green(path.join(process.cwd(), solution_test_file_path)),
        ].join(""));
    }
}

/**
 * 监听 .solutions 目录变化
 * REF: https://www.npmjs.com/package/chokidar
 */
const solutions_watcher = chokidar.watch(
    C.SOLUTIONS_TEMPORARY_DIRECTORY,
    {
        // ignored: /(^|[/\\])\../, // ignore dotfiles
    },
);

solutions_watcher.on("all", solutionsHandler);
