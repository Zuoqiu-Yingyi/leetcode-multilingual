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

/**
 * 题目难度
 */
export enum Difficulty {
    easy = "easy",
    hard = "hard",
    medium = "medium",
}

/**
 * 题解语言
 */
export enum Language {
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
export enum Extension {
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
 * 题解格式
 */
export enum SolutionFormat {
    class,
    function,
}
