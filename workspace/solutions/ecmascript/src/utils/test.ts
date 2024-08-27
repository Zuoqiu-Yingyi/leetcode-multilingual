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

import { C, E } from "@repo/common";

/**
 * 题解
 */
export interface ISolution<F extends CallableFunction = CallableFunction> {
    name: string;
    func: F;
}

/**
 * 测试用例 (类格式)
 */
export interface IExampleWithInit<
    INIT extends any[] = any[],
    I extends any[] = any[],
    O = any,
> {
    /**
     * 类初始化参数
     */
    init: INIT;
    /**
     * 函数参数列表
     */
    inputs: I[];
    /**
     * 函数返回值列表
     */
    outputs: O[];
}

/**
 * 测试用例 (函数格式)
 */
export interface IExample<
    I extends any[] = any[],
    O = any,
> {
    /**
     * 函数参数
     */
    input: I;
    /**
     * 函数返回值
     */
    output: O;
}

export type TExample = IExample | IExampleWithInit;

/**
 * 模块测试
 * @param test - 测试模块
 * @param dir - 目录
 * @param format - 题解格式
 * @param examples - 测试用例
 */
export async function t(
    test: typeof import("bun:test"),
    dir: string,
    format: E.SolutionFormat = E.SolutionFormat.function,
    examples?: TExample[],
) {
    // console.debug("t", arguments);

    const full_paths = dir.split(path.sep);
    const _paths = full_paths.slice(full_paths.length - C.ID_LENGTH);
    const paths = _paths.map((path) => path.replace(/^s*/, ""));
    const id = paths.join("");
    const examples_path = paths.join("/");
    const solutions_path = _paths.join("/");

    if (!examples) {
        const examples_file_path = path.join(
            process.cwd(),
            "./../../packages/examples",
            examples_path,
            `${id}.json`,
        );
        try {
            // !使用异步文件操作后调用 test 会导致其回调无法执行
            const json = fs.readFileSync(
                examples_file_path,
                "utf-8",
            );
            examples = JSON.parse(json) as IExample[];
        }
        catch (error) {
            void error;
            console.error(`Examples load failed: ${examples_file_path}`);
            return;
        }
    }

    // !使用异步文件操作后调用 test 会导致其回调无法执行
    const files = fs.readdirSync(dir);
    const names = files.filter((name) => C.SOLUTION_FILE_NAME_REGEXP.test(name));
    test.expect(
        names.length,
        "No solutions",
    ).toBeGreaterThan(0);

    const solutions = await Promise.all(names.map((name) => (async () => ({
        name,
        func: (await import(`@/${solutions_path}/${name}`)).default,
    }))()));

    let solution_index = 0;
    test.describe.each(solutions)(id, async (solution) => {
        // REF: https://github.com/oven-sh/bun/issues/13090
        // !多次调用 t 函数会导致 test 回调无法执行
        // *可以通过直接将 test 模块作为参数传入的方式解决
        switch (format) {
            case E.SolutionFormat.function: {
                const func = solution.func;
                let example_index = 0;
                test.test.each(examples as IExample[])(`${solution.name} - ${func.name}`, ({ input, output }) => {
                    const result = solution.func(...input);
                    test.expect(
                        result,
                        [
                            "",
                            `solution: ${solution_index}`,
                            `example:  ${example_index}`,
                            `input:    ${JSON.stringify(input)}`,
                            `result:   ${JSON.stringify(result)}`,
                            `expected: ${JSON.stringify(output)}`,
                            "",
                        ].join("\n"),
                    ).toEqual(output);
                    example_index++;
                });
                break;
            }

            case E.SolutionFormat.class: {
                const Solution = solution.func;
                let examples_index = 0;
                test.test.each(examples as IExampleWithInit[])(`${solution.name} - ${solution.func.name}`, ({ init, inputs, outputs }) => {
                    test.expect(
                        inputs.length,
                        `inputs.length != outputs.length`,
                    ).toEqual(outputs.length);

                    const solution = new Solution(...init);
                    inputs.forEach((input, example_index) => {
                        const result = solution.func(...input);
                        const output = outputs[example_index];
                        test.expect(
                            result,
                            [
                                "",
                                `solution: ${solution_index}`,
                                `examples: ${examples_index}`,
                                `example:  ${example_index}`,
                                `input:    ${JSON.stringify(input)}`,
                                `result:   ${JSON.stringify(result)}`,
                                `expected: ${JSON.stringify(output)}`,
                                "",
                            ].join("\n"),
                        ).toEqual(output);
                    });
                    examples_index++;
                });
                break;
            }

            default:
                break;
        }
        solution_index++;
    });
}
