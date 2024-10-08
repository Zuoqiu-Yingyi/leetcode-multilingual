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

import { C, E, U } from "@repo/common";
import {
    renderExamplesFile,
    renderSolutionFile,
    renderTestFile,
} from "@repo/templates";

import type fs from "node:fs";

import type { ISolutionInfo } from "@repo/common/types";

console.log(import.meta.url);

type TEntryEventName = "add" | "addDir" | "change" | "unlink" | "unlinkDir";

/**
 * 文件操作事件
 */
interface IFileEvent {
    path: string;
    name?: "add" | "change" | "unlink";
    index?: number;
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
 * 构造题解目录路径
 * @param language - 题解语言
 * @param id - 题解编号
 * @returns 目录路径
 */
function solutionDirectory(
    language: E.Language,
    id: number,
): string {
    const source_root_directory = C.SOLUTIONS_DIRECTORY_MAP.get(language);
    if (source_root_directory) {
        const paths = U.id2paths(id, C.ID_PREFIX);
        switch (language) {
            case E.Language.rust:
            case E.Language.java:
            case E.Language.kotlin:
            case E.Language.golang:
            case E.Language.python3:
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
    const source_root_directory = C.SOLUTIONS_TEST_DIRECTORY_MAP.get(language);
    if (source_root_directory) {
        const paths = U.id2paths(id, C.ID_PREFIX);
        switch (language) {
            case E.Language.rust:
            case E.Language.java:
            case E.Language.kotlin:
            case E.Language.golang:
            case E.Language.python3:
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
    const name = `s_${String(info.id).padStart(C.ID_LENGTH, "0")}_${String(index).padStart(C.SOLUTION_INDEX_LENGTH, "0")}`;
    switch (info.language) {
        case E.Language.java:
        case E.Language.kotlin:
        case E.Language.golang:
            return `${name}/${name}${info.ext}`;
        case E.Language.rust:
        case E.Language.python3:
        case E.Language.javascript:
        case E.Language.typescript:
        default:
            return `${name}${info.ext}`;
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
): Promise<IFileEvent | void> {
    try {
        const solution_directory_path = solutionDirectory(info.language, info.id);

        for (let index = 0; true; index++) {
            const solution_file_path = path.join(solution_directory_path, solutionFileName(info, index));
            if (await fsAsync.exists(solution_file_path)) {
                continue;
            }
            else {
                await Bun.sleep(1_000); // 避免 VSCode 扩展 LeetCode.vscode-leetcode 重复创建文件

                /* 在指定位置创建题解模板文件 */
                const content = await (async () => {
                    let retry = 3;
                    while (retry--) {
                        try {
                            const content = await Bun.file(original).text();
                            if (content) {
                                return content;
                            }
                        }
                        catch (error) {
                            console.warn(error);
                        }
                        await Bun.sleep(1_000);
                    }
                    throw new Error(`Failed to read file: ${original}`);
                })();

                const solution_file_content = await renderSolutionFile(
                    {
                        ...info,
                        index,
                    },
                    content,
                ) ?? content.replaceAll("\r\n", "\n");
                await Bun.write(
                    solution_file_path,
                    solution_file_content,
                    { createPath: true },
                );

                /* 删除 VSCode 插件 LeetCode.vscode-leetcode 创建的题解模板文件 */
                await fsAsync.unlink(original);
                return {
                    path: solution_file_path,
                    name: "add",
                    index,
                };
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
): Promise<IFileEvent | void> {
    try {
        const test_directory_path = solutionTestDirectory(info.language, info.id);
        const id = U.idPadZero(info.id);

        const test_file_path = (() => {
            switch (info.language) {
                /* 创建测试文件 */
                case E.Language.rust:
                case E.Language.java:
                case E.Language.kotlin:
                case E.Language.golang:
                case E.Language.python3:
                    return path.join(test_directory_path, `s_${id}_test${info.ext}`);

                case E.Language.javascript:
                case E.Language.typescript:
                    return path.join(test_directory_path, `s_${id}_test.ts`);
                default:
                    return null;
            }
        })();
        if (test_file_path) {
            if (!(await fsAsync.exists(test_file_path))) {
                const test_file_content = await renderTestFile(info);
                if (test_file_content) {
                    await Bun.write(
                        test_file_path,
                        test_file_content,
                        { createPath: true },
                    );
                    return {
                        path: test_file_path,
                        name: "add",
                    };
                }
            }
            else {
                return {
                    path: test_file_path,
                };
            }
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
async function createExamplesFile(info: ISolutionInfo): Promise<IFileEvent | void> {
    const id = U.idPadZero(info.id);
    const paths = U.id2paths(info.id);
    const examples_file_path = path.join(C.SOLUTIONS_TEST_EXAMPLES_DIRECTORY, ...paths, `${id}.json`);
    if (!(await fsAsync.exists(examples_file_path))) {
        const examples_content = await renderExamplesFile(info);
        if (examples_content) {
            await Bun.write(
                examples_file_path,
                examples_content,
                { createPath: true },
            );
            return {
                path: examples_file_path,
                name: "add",
            };
        }
        else {
            throw new Error(`Failed to render examples file: ${examples_file_path}`);
        }
    }
    else {
        return {
            path: examples_file_path,
        };
    }
}

/**
 * 事件名称文本宽度
 */
const EVENT_NAME_WIDTH = 10;

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
 * 格式化文件事件
 * @param event - 文件事件
 */
function formatFileEvent(event: IFileEvent): string {
    const full_path = path.join(process.cwd(), event.path);
    switch (event.name) {
        case "add":
            return chalk.green(full_path);
        case "change":
            return chalk.cyan(full_path);
        case "unlink":
            return chalk.red(full_path);
        default:
            return full_path;
    }
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
    let examples_file: IFileEvent | void = void null;
    let solution_file: IFileEvent | void = void null;
    let solution_test_file: IFileEvent | void = void null;
    switch (eventName) {
        case "add": {
            const file_info = parseSolutionFilePath(entryPath);
            if (file_info) {
                // console.log(file_info);
                switch (file_info.language) {
                    case E.Language.rust:
                    case E.Language.java:
                    case E.Language.golang:
                    case E.Language.kotlin:
                    case E.Language.python3:
                    case E.Language.javascript:
                    case E.Language.typescript: {
                        examples_file = await createExamplesFile(file_info);
                        solution_file = await createSolutionFile(file_info, entryPath);
                        solution_test_file = await createSolutionTestFile(file_info);
                        break;
                    }
                }
            }
            break;
        }
        default:
            break;
    }
    if (examples_file) {
        console.debug([
            "🌰  ".padStart(EVENT_NAME_WIDTH),
            formatFileEvent(examples_file),
        ].join(""));
    }
    if (solution_file) {
        console.debug([
            "➜  ".padStart(EVENT_NAME_WIDTH),
            formatFileEvent(solution_file),
        ].join(""));
    }
    if (solution_test_file) {
        console.debug([
            "🧪  ".padStart(EVENT_NAME_WIDTH),
            formatFileEvent(solution_test_file),
        ].join(""));
    }
}

/**
 * 监听临时题解目录
 * REF: https://www.npmjs.com/package/chokidar
 */
const temporary_solutions_watcher = chokidar.watch(
    C.SOLUTIONS_TEMPORARY_DIRECTORY,
    {
        // ignored: /(^|[/\\])\../, // ignore dot-files
    },
);

temporary_solutions_watcher.on("all", solutionsHandler);

// TODO: 监听 solutions/go/**/s_*_*.go 变化
// 更新 solutions/go/**/s_*_test.go 文件并运行测试

// TODO: 监听 solutions/java/lib/src/main/java/**/s_*_*.java 变化
// 更新 solutions/java/lib/src/test/java/**/s_*_test.java 文件并运行测试

// TODO: 监听 solutions/kotlin/lib/src/main/kotlin/**/s_*_*.kt 变化
// 更新 solutions/kotlin/lib/src/test/kotlin/**/s_*_test.kt 文件并运行测试

// TODO: 监听 solutions/rust/src/**/s_*_*.rs 变化
// 更新 solutions/rust/src/**/s_*_test.rs 与 solutions/rust/src/**/mod.rs 文件并运行测试
