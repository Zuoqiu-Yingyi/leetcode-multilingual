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

import type fs from "node:fs";

console.log(import.meta.url);

/**
 * 题目难度
 */
enum Difficulty {
    easy = "easy",
    hard = "hard",
    medium = "medium",
}

/**
 * 题解语言
 */
enum Language {
    bash = "bash",
    c = "c",
    cpp = "cpp",
    csharp = "csharp",
    golang = "golang",
    java = "java",
    javascript = "javascript",
    kotlin = "kotlin",
    mysql = "mysql",
    php = "php",
    python = "python",
    python3 = "python3",
    ruby = "ruby",
    rust = "rust",
    scala = "scala",
    swift = "swift",
    typescript = "typescript",
}

/**
 * 题解文件扩展名
 */
enum Extension {
    sh = "sh",
    c = "c",
    cpp = "cpp",
    cs = "cs",
    go = "go",
    java = "java",
    js = "js",
    kt = "kt",
    sql = "sql",
    php = "php",
    py = "py",
    rb = "rb",
    rs = "rs",
    scala = "scala",
    swift = "swift",
    ts = "ts",
}

/**
 * 题解信息
 */
interface ISolutionInfo {
    id: number;
    difficulty: Difficulty;
    name: string;
    language: Language;
    ext: Extension;
}

/**
 * 题解临时目录
 */
const SOLUTIONS_TEMPORARY_DIRECTORY = ".solutions";

/**
 * 题解目录
 */
const SOLUTIONS_DIRECTORY = {
    [Language.javascript]: "./solutions/ecmascript/src",
    [Language.typescript]: "./solutions/ecmascript/src",
} as const;

/**
 * 解析题解文件路径
 * @param filePath - 文件相对路径
 * @returns 题解信息
 */
function parseSolutionFilePath(filePath: string): ISolutionInfo | void {
    const { dir, name: language, ext } = path.parse(filePath);
    const [directory, problem] = dir.split(path.sep);
    if (directory === SOLUTIONS_TEMPORARY_DIRECTORY && problem) {
        const [id, difficulty, name] = problem.split(".");
        if (id && difficulty && name) {
            return {
                id: Number.parseInt(id),
                difficulty: difficulty as Difficulty,
                name,
                language: language as Language,
                ext: ext as Extension,
            } satisfies ISolutionInfo;
        }
    }
}

/**
 * 构造题解目录
 * @param language - 题解语言
 * @param id - 题解编号
 * @returns 目录路径
 */
function solutionDirectory(
    language: Language,
    id: number,
): string {
    // @ts-expect-error info is not assignable to type 'never'
    const source_root_directory: string | undefined = SOLUTIONS_DIRECTORY[language];
    if (source_root_directory) {
        const paths = String(id).padStart(4, "0").split("");
        switch (language) {
            case Language.javascript:
            case Language.typescript:
            default:
                return path.join(source_root_directory, ...paths);
        }
    }
    throw new Error(`Unsupported language: ${language}`);
}

/**
 * 构造题解文件名
 * @param language - 题解语言
 * @param index - 题解序号
 * @param ext - 扩展名
 * @returns 文件名
 */
function solutionFileName(
    language: Language,
    index: number,
    ext: Extension,
): string {
    switch (language) {
        case Language.javascript:
        case Language.typescript:
        default:
            return `solution${index}${ext}`;
    }
}

/**
 * 移动题解文件
 * @param info - 题解信息
 * @param original - 原文件路径
 * @returns 新文件路径
 */
async function moveSolutionFile(
    info: ISolutionInfo,
    original: string,
): Promise<string | void> {
    try {
        const target_directory_path = solutionDirectory(info.language, info.id);

        switch (info.language) {
            case Language.javascript:
            case Language.typescript: {
                for (let index = 1; true; index++) {
                    const target_file_path = path.join(target_directory_path, solutionFileName(info.language, index, info.ext));
                    if (await fsAsync.exists(target_file_path)) {
                        continue;
                    }
                    else {
                        await fsAsync.mkdir(target_directory_path, { recursive: true });
                        await fsAsync.rename(original, target_file_path);
                        return target_file_path;
                    }
                }
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
    let target_file_path: string | void = void null;
    switch (eventName) {
        case "add": {
            const file_info = parseSolutionFilePath(entryPath);
            if (file_info) {
                // console.log(file_info);
                target_file_path = await moveSolutionFile(file_info, entryPath);
            }
            break;
        }
        default:
            break;
    }
    if (target_file_path) {
        console.debug([
            // " ".repeat(EVENT_NAME_WIDTH),
            "➜  ".padStart(EVENT_NAME_WIDTH),
            chalk.green(path.join(process.cwd(), target_file_path)),
        ].join(""));
    }
}

/**
 * 监听 .solutions 目录变化
 * REF: https://www.npmjs.com/package/chokidar
 */
const solutions_watcher = chokidar.watch(
    SOLUTIONS_TEMPORARY_DIRECTORY,
    {
        // ignored: /(^|[/\\])\../, // ignore dotfiles
    },
);

solutions_watcher.on("all", solutionsHandler);
