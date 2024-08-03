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
import { describe, expect, test } from "bun:test";
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
 * @param dir - 目录
 * @param examples - 测试用例
 */
export async function t<
    I extends any[] = any[],
    O = any,
>(
    dir: string,
    examples?: IExample<I, O>[],
) {
    // console.debug("t", arguments);

    const paths = dir.split(path.sep);
    const paths_ = paths.slice(paths.length - C.ID_LENGTH);
    const id = paths_.join("");
    const cwd = paths_.join("/");

    if (!examples) {
        const json = fs.readFileSync(path.join(
            process.cwd(),
            "./../../packages/examples",
            cwd,
            `${id}.json`,
        ), "utf-8");
        examples = JSON.parse(json) as IExample<I, O>[];
    }

    const files = fs.readdirSync(dir);
    const names = files.filter((name) => /^\d+-\d+\.(?:js|ts)$/.test(name));
    const solutions = await Promise.all(names.map((name) => (async () => ({
        name,
        func: (await import(`@/${cwd}/${name}`)).default,
    }))()));

    describe.each(solutions)(id, ({ name, func }) => {
        // !使用异步文件操作后调用 test 会导致其回调无法执行
        test.each(examples)(`${name} - ${func.name}`, ({ input, output }) => {
            expect(
                func(...input),
                    `${func.name}(${JSON.stringify(input).slice(1, -1)}) != ${JSON.stringify(output)}`,
            ).toEqual(output);
        });
    });
}
