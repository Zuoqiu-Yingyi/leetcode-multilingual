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

export const TEMPLATES_DIRECTORY_NAME = "templates";
export const TEMPLATES_TEST_DIRECTORY_NAME = "test";
export const TEMPLATES_SOLUTION_DIRECTORY_NAME = "solution";

export const TEMPLATE_FILE_NAME = {
    [E.Language.golang]: "golang.mustache",
    [E.Language.javascript]: "ecmascript.mustache",
    [E.Language.typescript]: "ecmascript.mustache",
} as const;

export const TEMPLATE_FILE_NAME_MAP = new Map(Object.entries(TEMPLATE_FILE_NAME)) as Map<E.Language, string>;

export const MUSTACHE_TAGS: OpeningAndClosingTags = ["/*{{", "}}*/"] as const;

export interface ITestInfo {
    language: E.Language;
    id: number;
    index?: number;
}

export async function renderTestFile(info: ITestInfo): Promise<string | void> {
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
            return Mustache.render(template_content, {
                id: U.idPadZero(info.id),
                path: U.id2paths(info.id, "_").join("/"),
                index: U.idPadZero(info.index ?? 1, C.SOLUTION_INDEX_LENGTH),
            }, undefined, {
                escape: (value) => value,
                tags: MUSTACHE_TAGS,
            });
        }
    }
}
