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

// REF: https://bun.sh/docs/cli/test

import fs from "node:fs";
import path from "node:path";
import process from "node:process";

import { C } from "@repo/common";

/**
 * 题解
 */
export interface ISolution<F extends CallableFunction = CallableFunction> {
    name: string;
    func: F;
}

/**
 * 测试用例
 */
export interface IExample<
    I extends any[] = any[],
    O = any,
> {
    input: I;
    output: O;
}

/**
 * 模块测试
 * @param test - 测试模块
 * @param dir - 目录
 * @param examples - 测试用例
 */
export async function t<
    I extends any[] = any[],
    O = any,
>(
    test: typeof import("bun:test"),
    dir: string,
    examples?: IExample<I, O>[],
) {
    // console.debug("t", arguments);

    const paths = dir.split(path.sep);
    const paths_ = paths.slice(paths.length - C.ID_LENGTH);
    const id = paths_.join("");
    const cwd = paths_.join("/");

    if (!examples) {
        const examples_file_path = path.join(
            process.cwd(),
            "./../../packages/examples",
            cwd,
            `${id}.json`,
        );
        try {
            const json = fs.readFileSync(
                examples_file_path,
                "utf-8",
            );
            examples = JSON.parse(json) as IExample<I, O>[];
        }
        catch (error) {
            void error;
            console.error(`Examples load failed: ${examples_file_path}`);
            return;
        }
    }

    const files = fs.readdirSync(dir);
    const names = files.filter((name) => /^\d+-\d+\.(?:js|ts)$/.test(name));
    const solutions = await Promise.all(names.map((name) => (async () => ({
        name,
        func: (await import(`@/${cwd}/${name}`)).default,
    }))()));

    test.describe.each(solutions)(id, async ({ name, func }) => {
        // REF: https://github.com/oven-sh/bun/issues/13090
        // !使用异步文件操作后调用 test 会导致其回调无法执行
        // !多次调用 t 函数也会导致 test 回调无法执行
        // *可以通过直接将 test 模块作为参数传入的方式解决
        test.test.each(examples)(`${name} - ${func.name}`, ({ input, output }) => {
            test.expect(
                func(...input),
                `${func.name}(${JSON.stringify(input).slice(1, -1)}) != ${JSON.stringify(output)}`,
            ).toEqual(output);
        });
    });
}
